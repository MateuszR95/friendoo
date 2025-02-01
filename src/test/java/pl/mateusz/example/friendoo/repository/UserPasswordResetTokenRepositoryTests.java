package pl.mateusz.example.friendoo.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import pl.mateusz.example.friendoo.gender.Gender;
import pl.mateusz.example.friendoo.gender.UserGender;
import pl.mateusz.example.friendoo.gender.UserGenderRepository;
import pl.mateusz.example.friendoo.user.User;
import pl.mateusz.example.friendoo.user.UserRepository;
import pl.mateusz.example.friendoo.user.passwordreset.UserPasswordResetToken;
import pl.mateusz.example.friendoo.user.passwordreset.UserPasswordResetTokenRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
public class UserPasswordResetTokenRepositoryTests {

  @Autowired
  UserPasswordResetTokenRepository userPasswordResetTokenRepository;

  @Autowired
  UserGenderRepository userGenderRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  TestEntityManager entityManager;

  private User user;

  private User user2;

  private User user3;

  private UserPasswordResetToken validToken1;

  private UserPasswordResetToken validToken2;

  private UserPasswordResetToken invalidToken1;

  private UserPasswordResetToken invalidToken2;

  private UserPasswordResetToken invalidToken3;

  private UserPasswordResetToken invalidToken4;

  @BeforeEach
  void setup() {
    UserGender userGender = initUserGender();
    initUsers(userGender);
    initPasswordResetTokens();
  }

  @Nested
  class findFirstByUserEmailAndIsUsedFalseAndExpireDateIsAfterOrderByCreationDateDescTests {

    @Test
    public void shouldReturnOptionalWithFirstAndValidUserPasswordResetTokenWhenUserHasTwoValidTokens() {
      // given
      // when
      Optional<UserPasswordResetToken> result = userPasswordResetTokenRepository
        .findFirstByUserEmailAndIsUsedFalseAndExpireDateIsAfterOrderByCreationDateDesc(
        user.getEmail(), LocalDateTime.now());
      // then
      assertTrue(result.isPresent());
      UserPasswordResetToken userPasswordResetToken = result.get();
      assertEquals(userPasswordResetToken.getId(), validToken2.getId());
      assertEquals(userPasswordResetToken.getToken(), validToken2.getToken());
      assertEquals(userPasswordResetToken.getUser().getId(), user.getId());
    }

    @Test
    public void shouldReturnEmptyOptionalWhenUserDoesNotHaveAnyToken() {
      // given
      // when
      Optional<UserPasswordResetToken> result = userPasswordResetTokenRepository
        .findFirstByUserEmailAndIsUsedFalseAndExpireDateIsAfterOrderByCreationDateDesc(
          user2.getEmail(), LocalDateTime.now());
      // then
      assertTrue(result.isEmpty());
    }

    @Test
    public void shouldReturnEmptyOptionalWhenUserEmailDoesNotExist() {
      // given
      String notExistingEmail = "notexisting@byom.de";
      // when
      Optional<UserPasswordResetToken> result = userPasswordResetTokenRepository
        .findFirstByUserEmailAndIsUsedFalseAndExpireDateIsAfterOrderByCreationDateDesc(
          notExistingEmail, LocalDateTime.now());
      // then
      assertTrue(result.isEmpty());
    }

    @Test
    public void shouldReturnEmptyOptionalWhenUserHasOneAlreadyUsedToken() {
      // given
      // when
      Optional<UserPasswordResetToken> result = userPasswordResetTokenRepository
        .findFirstByUserEmailAndIsUsedFalseAndExpireDateIsAfterOrderByCreationDateDesc(
          user2.getEmail(), LocalDateTime.now());
      // then
      assertTrue(result.isEmpty());
    }

    @Test
    public void shouldReturnEmptyOptionalWhenUserHasOneUnusedAndExpiredToken() {
      // given
      // when
      Optional<UserPasswordResetToken> result = userPasswordResetTokenRepository
        .findFirstByUserEmailAndIsUsedFalseAndExpireDateIsAfterOrderByCreationDateDesc(
          user3.getEmail(), LocalDateTime.now());
      // then
      assertTrue(result.isEmpty());
    }

  }

  @Nested
  class getAllByIsValidTrueTests {

    @Test
    public void shouldReturnListWithTwoTokensWhenTwoValidTokensExists() {
      // given
      // when
      List<UserPasswordResetToken> validTokens = userPasswordResetTokenRepository.getAllByIsValidTrue();
      List<Long> tokenIdList = validTokens.stream().map(UserPasswordResetToken::getId).toList();
      // then
      assertEquals(2, validTokens.size());
      assertTrue(tokenIdList.containsAll(List.of(validToken1.getId(), validToken2.getId())));
    }

    @Test
    public void shouldReturnEmptyListWhenAllTokensAreInvalid() {
      // given
      validToken1.setValid(false);
      validToken2.setValid(false);
      userPasswordResetTokenRepository.saveAll(List.of(validToken1, validToken2));
      // when
      List<UserPasswordResetToken> validTokens = userPasswordResetTokenRepository.getAllByIsValidTrue();
      // then
      assertTrue(validTokens.isEmpty());
    }

    @Test
    public void shouldReturnEmptyListWhenAnyTokenExists() {
      // given
      userPasswordResetTokenRepository.deleteAll();
      // when
      List<UserPasswordResetToken> validTokens = userPasswordResetTokenRepository.getAllByIsValidTrue();
      // then
      assertTrue(validTokens.isEmpty());
    }

  }

  @Nested
  class getAllByUserAndIsValidTrueTests {

    @Test
    public void shouldReturnListWithTwoValidTokensWhenUserHasTwoValidTokens() {
      // given
      // when
      List<UserPasswordResetToken> result = userPasswordResetTokenRepository.getAllByUserAndIsValidTrue(user);
      // then
      assertEquals(result.size(), 2);
    }

    @Test
    public void shouldReturnEmptyListWhenUserDoesNotHaveValidTokens() {
      // given
      // when
      List<UserPasswordResetToken> result = userPasswordResetTokenRepository.getAllByUserAndIsValidTrue(user2);
      // then
      assertTrue(result.isEmpty());
    }

    @Test
    public void shouldReturnEmptyListWhenUserDoesNotExist() {
      // given
      userRepository.delete(user3);
      assertFalse(userRepository.existsUserByEmail(user3.getEmail()));
      // when
      List<UserPasswordResetToken> result = userPasswordResetTokenRepository.getAllByUserAndIsValidTrue(user3);
      // then
      assertTrue(result.isEmpty());
    }

  }

  @Nested
  class getByTokenAndIsValidTrueTests {

    @Test
    public void shouldReturnOptionalWithValidUserPasswordResetTokenWhenTokenExistsAndIsValid() {
      // given
      String tokenCode = validToken1.getToken();
      // when
      Optional<UserPasswordResetToken> result = userPasswordResetTokenRepository.getByTokenAndIsValidTrue(tokenCode);
      // token
      assertTrue(result.isPresent());
      UserPasswordResetToken userPasswordResetToken = result.get();
      assertEquals(userPasswordResetToken.getToken(), tokenCode);
      assertEquals(userPasswordResetToken.getId(), validToken1.getId());
    }

    @Test
    public void shouldReturnEmptyOptionalWhenTokenCodeDoesNotExist() {
      // given
      String notExistingTokenCode = "notexisting";
      // when
      Optional<UserPasswordResetToken> result = userPasswordResetTokenRepository
        .getByTokenAndIsValidTrue(notExistingTokenCode);
      // token
      assertTrue(result.isEmpty());
    }

    @Test
    public void shouldReturnEmptyOptionalWhenTokenCodeExistsAndTokenIsInvalid() {
      // given
      String tokenCode = invalidToken1.getToken();
      // when
      Optional<UserPasswordResetToken> result = userPasswordResetTokenRepository
        .getByTokenAndIsValidTrue(tokenCode);
      // token
      assertTrue(result.isEmpty());
    }

  }
  @Nested
  class getByTokenTests {

    @Test
    public void shouldReturnOptionalWithUserPasswordResetTokenWhenTokenCodeExists() {
      // given
      String tokenCode = validToken1.getToken();
      // when
      Optional<UserPasswordResetToken> result = userPasswordResetTokenRepository.getByToken(tokenCode);
      // then
      assertTrue(result.isPresent());
      UserPasswordResetToken userPasswordResetToken = result.get();
      assertEquals(userPasswordResetToken.getToken(), tokenCode);
      assertEquals(userPasswordResetToken.getId(), validToken1.getId());
    }

    @Test
    public void shouldReturnEmptyOptionalWhenTokenCodeDoesNotExist() {
      // given
      String notExistingTokenCode = "notexistingcode";
      // when
      Optional<UserPasswordResetToken> result = userPasswordResetTokenRepository.getByToken(notExistingTokenCode);
      // then
      assertTrue(result.isEmpty());
    }

    @Test
    public void shouldReturnEmptyOptionalWhenTokenCodeIsNull() {
      // given
      String nullTokenCode = null;
      // when
      Optional<UserPasswordResetToken> result = userPasswordResetTokenRepository.getByToken(nullTokenCode);
      // then
      assertTrue(result.isEmpty());
    }

    @Test
    public void shouldReturnEmptyOptionalWhenTokenCodeIsEmpty() {
      // given
      String emptyTokenCode = "";
      // when
      Optional<UserPasswordResetToken> result = userPasswordResetTokenRepository.getByToken(emptyTokenCode);
      // then
      assertTrue(result.isEmpty());
    }

  }



  private UserGender initUserGender() {
    UserGender userGender = new UserGender();
    userGender.setId(1L);
    userGender.setGender(Gender.MAN);
    userGenderRepository.save(userGender);
    return userGender;
  }
  private void initUsers(UserGender userGender) {
    user = createTestUser("Tomasz", "Nowak", "tnowak@byom.de",
      LocalDate.of(1993, 10, 20), userGender, "password",
      LocalDateTime.now().minusDays(15), true);
    user2 = createTestUser("Adam", "Kowalski", "akowal@byom.de",
      LocalDate.of(1995, 2, 11), userGender, "password",
      LocalDateTime.now().minusDays(1), true);
    user3 = createTestUser("Igor", "Wolski", "iwolski@byom.de",
      LocalDate.of(1991, 5, 12), userGender, "password123",
      LocalDateTime.now().minusDays(100), true);
    userRepository.save(user);
    userRepository.save(user2);
    userRepository.save(user3);
  }

  private void initPasswordResetTokens() {
    validToken1 = createUserPasswordResetToken("qwert1235", LocalDateTime.now().minusMinutes(60),
      LocalDateTime.now().plusDays(1), user, null, false, true);
    validToken2 = createUserPasswordResetToken("qwert2111", LocalDateTime.now().minusMinutes(30),
      LocalDateTime.now().plusDays(1), user, null, false, true);
    invalidToken1 = createUserPasswordResetToken("qwert9876", LocalDateTime.now().minusMinutes(180),
      LocalDateTime.now().plusDays(1), user, LocalDateTime.now().minusMinutes(5), true, false);
    invalidToken2 = createUserPasswordResetToken("qwert7777", LocalDateTime.now().minusMinutes(380),
      LocalDateTime.now().plusDays(1), user, LocalDateTime.now().minusMinutes(15), true, false);
    invalidToken3 = createUserPasswordResetToken("qwert0000", LocalDateTime.now().minusDays(1),
      LocalDateTime.now().plusDays(1), user2, LocalDateTime.now().minusMinutes(15), true, false);
    invalidToken4 = createUserPasswordResetToken("qwert0110", LocalDateTime.now().minusDays(1),
      LocalDateTime.now().minusMinutes(60), user3, null, false, false);
    userPasswordResetTokenRepository.save(validToken1);
    userPasswordResetTokenRepository.save(validToken2);
    userPasswordResetTokenRepository.save(invalidToken1);
    userPasswordResetTokenRepository.save(invalidToken3);
    userPasswordResetTokenRepository.save(invalidToken4);
  }

  private User createTestUser(String firstName, String lastName, String email, LocalDate dateOfBirth,
                              UserGender userGender, String password, LocalDateTime joinedAt, boolean isActiveAccount) {
    User user = new User();
    user.setFirstName(firstName);
    user.setLastName(lastName);
    user.setEmail(email);
    user.setDateOfBirth(dateOfBirth);
    user.setGender(userGender);
    user.setPassword(password);
    user.setJoinedAt(joinedAt);
    user.setActiveAccount(isActiveAccount);
    return user;
  }

  private UserPasswordResetToken createUserPasswordResetToken(String tokenCode, LocalDateTime creationDate,
                                                              LocalDateTime expirationDate, User user,
                                                              LocalDateTime usedDate, boolean isUsed, boolean isValid) {
    UserPasswordResetToken userPasswordResetToken = new UserPasswordResetToken();
    userPasswordResetToken.setToken(tokenCode);
    userPasswordResetToken.setCreationDate(creationDate);
    userPasswordResetToken.setExpireDate(expirationDate);
    userPasswordResetToken.setUser(user);
    userPasswordResetToken.setUsedDate(usedDate);
    userPasswordResetToken.setUsed(isUsed);
    userPasswordResetToken.setValid(isValid);
    return userPasswordResetToken;
  }
}
