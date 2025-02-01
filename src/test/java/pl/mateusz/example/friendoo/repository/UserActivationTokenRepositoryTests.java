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
import pl.mateusz.example.friendoo.user.activation.UserActivationToken;
import pl.mateusz.example.friendoo.user.activation.UserActivationTokenRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserActivationTokenRepositoryTests {

  @Autowired
  UserActivationTokenRepository userActivationTokenRepository;

  @Autowired
  UserGenderRepository userGenderRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  TestEntityManager entityManager;

  private User user;

  private User user2;

  private User user3;

  private final UserActivationToken userActivationToken = new UserActivationToken();

  private final UserActivationToken expiredToken = new UserActivationToken();

  @BeforeEach
  void setup() {
    UserGender userGender = initUserGender();
    initUsers(userGender);
    initUserActivationTokens();
  }

  @Nested
  class getUserActivationTokenByUserEmailTests {

    @Test
    void shouldReturnOptionalWithUserActivationTokenWhenUserActivationTokenExists() {
      // given
      // when
      Optional<UserActivationToken> userActivationTokenByUserEmail = userActivationTokenRepository
            .getUserActivationTokenByUserEmail(user.getEmail());
      // then
      assertTrue(userActivationTokenByUserEmail.isPresent());
      assertEquals(userActivationTokenByUserEmail.get().getUser().getEmail(), user.getEmail());
    }

    @Test
    void shouldReturnEmptyOptionalWhenUserActivationTokenNotExistsForProvidedUser() {
      // given
      // when
      Optional<UserActivationToken> userActivationTokenByUserEmail = userActivationTokenRepository
        .getUserActivationTokenByUserEmail(user3.getEmail());
      // then
      assertTrue(userActivationTokenByUserEmail.isEmpty());
    }

    @Test
    void shouldReturnEmptyOptionalWhenProvidedUserEmailIsEmpty() {
      // given
      String emptyEmail = "";
      // when
      Optional<UserActivationToken> userActivationTokenByUserEmail = userActivationTokenRepository
        .getUserActivationTokenByUserEmail(emptyEmail);
      // then
      assertTrue(userActivationTokenByUserEmail.isEmpty());
    }
  }

  @Nested
  class deleteExpiredTokensTests {

    @Test
    public void shouldDeleteExpiredTokens() {
      // given
      List<UserActivationToken> allTokens = userActivationTokenRepository.findAll();
      // when
      userActivationTokenRepository.deleteExpiredTokens(LocalDateTime.now());
      List<UserActivationToken> validTokens = userActivationTokenRepository.findAll();
      List<Long> validTokensIdList = validTokens.stream().map(UserActivationToken::getId).toList();
      // then
      assertTrue(validTokensIdList.contains(userActivationToken.getId()));
      assertFalse(validTokensIdList.contains(expiredToken.getId()));
      assertEquals(2, allTokens.size());
      assertEquals(1, validTokens.size());

    }

  }

  @Nested
  class deleteByUserEmailTests {

    @Test
    public void shouldDeleteUserActivationTokenByUserEmail() {
      // given
      List<UserActivationToken> allTokens = userActivationTokenRepository.findAll();
      // when
      userActivationTokenRepository.deleteByUserEmail(user.getEmail());
      List<UserActivationToken> adjustedTokenList = userActivationTokenRepository.findAll();
      List<Long> userIdList = adjustedTokenList.stream().map(u -> u.getUser().getId()).toList();
      List<String> userEmailList = adjustedTokenList.stream().map(u -> u.getUser().getEmail()).toList();
      // then
      assertEquals(allTokens.size(), 2);
      assertEquals(adjustedTokenList.size(), 1);
      assertFalse(userIdList.contains(user.getId()));
      assertFalse(userEmailList.contains(user.getEmail()));
    }

    @Test
    public void shouldNotDeleteUserActivationTokenByUserEmailWhenEmailIsNull() {
      // given
      List<UserActivationToken> allTokens = userActivationTokenRepository.findAll();
      // when
      userActivationTokenRepository.deleteByUserEmail(null);
      List<UserActivationToken> adjustedTokenList = userActivationTokenRepository.findAll();
      List<Long> userIdList = adjustedTokenList.stream().map(u -> u.getUser().getId()).toList();
      List<String> userEmailList = adjustedTokenList.stream().map(u -> u.getUser().getEmail()).toList();
      // then
      assertEquals(adjustedTokenList.size(), 2);
      assertEquals(allTokens.size(), adjustedTokenList.size());
      assertTrue(userIdList.contains(user.getId()));
      assertTrue(userEmailList.contains(user.getEmail()));
      assertTrue(userIdList.contains(user2.getId()));
      assertTrue(userEmailList.contains(user2.getEmail()));
    }

    @Test
    public void shouldNotDeleteUserActivationTokenWhenUserDoesNotExist() {
      // given
      String notExistingEmail = "notexisting@byom.de";
      List<UserActivationToken> allTokensBeforeDeleting = userActivationTokenRepository.findAll();
      // when
      userActivationTokenRepository.deleteByUserEmail(notExistingEmail);
      List<UserActivationToken> allTokensAfterDeleting = userActivationTokenRepository.findAll();
      // then
      assertEquals(allTokensBeforeDeleting.size(), allTokensAfterDeleting.size());
    }
  }

  private void initUserActivationTokens() {
    userActivationToken.setToken("abcdf");
    userActivationToken.setUser(user);
    userActivationToken.setCreationDate(user.getJoinedAt());
    userActivationToken.setExpireDate(user.getJoinedAt().plusMinutes(10));
    userActivationTokenRepository.save(userActivationToken);

    expiredToken.setToken("xxxxxxx");
    expiredToken.setUser(user2);
    expiredToken.setCreationDate(user2.getJoinedAt());
    expiredToken.setExpireDate(user2.getJoinedAt().plusMinutes(15));
    userActivationTokenRepository.save(expiredToken);
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
      LocalDateTime.now().minusMinutes(5), false);
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
}
