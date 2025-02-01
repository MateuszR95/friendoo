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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTests {

  @Autowired
  UserRepository userRepository;

  @Autowired
  UserGenderRepository userGenderRepository;

  @Autowired
  TestEntityManager entityManager;

  private User user;
  private User user2;
  private User user3;
  private User user4;

  @BeforeEach
  void setup() {
    UserGender userGender = initUserGender();
    initUsers(userGender);
    entityManager.flush();
  }

  @Nested
  class findUserByEmailTests {
    @Test
    public void shouldReturnOptionalWithUserWhenUserExists() {
      // when
      Optional<User> result = userRepository.findUserByEmail(user.getEmail());
      // then
      assertTrue(result.isPresent());
      assertEquals(result.get().getEmail(), user.getEmail());
      assertEquals(result.get().getFirstName(), user.getFirstName());
      assertEquals(result.get().getLastName(), user.getLastName());
    }

    @Test
    public void shouldReturnEmptyOptionalWhenUserDoesNotExist() {
      // given
      String notExistingEmail = "test123@byom.de";
      // when
      Optional<User> result = userRepository.findUserByEmail(notExistingEmail);
      // then
      assertTrue(result.isEmpty());
    }

    @Test
    public void shouldReturnEmptyOptionalWhenEmailIsNull() {
      // given
      // when
      Optional<User> result = userRepository.findUserByEmail(null);
      // then
      assertTrue(result.isEmpty());
    }

  }

  @Nested
  class existsUserByEmailTests {

    @Test
    public void shouldReturnTrueWhenUserExists() {
      // given
      // when
      boolean result = userRepository.existsUserByEmail(user.getEmail());
      // then
      assertTrue(result);
    }

    @Test
    public void shouldReturnFalseWhenUserNotExist() {
      // given
      String notExistingEmail = "test123@byom.de";
      // when
      boolean result = userRepository.existsUserByEmail(notExistingEmail);
      // then
      assertFalse(result);
    }

    @Test
    public void shouldReturnFalseWhenEmailIsNull() {
      // given
      // when
      boolean result = userRepository.existsUserByEmail(null);
      // then
      assertFalse(result);
    }

    @Test
    public void shouldReturnFalseWhenEmailIsEmpty() {
      // given
      String emptyEmail = "";
      // when
      boolean result = userRepository.existsUserByEmail(emptyEmail);
      // then
      assertFalse(result);
    }

  }

  @Nested
  class findFriendsByEmailTests {

    @Test
    public void shouldReturnListWithOneUserWhenUserHasOneFriend() {
      // given
      // when
      List<User> friendsByEmail = userRepository.findFriendsByEmail(user2.getEmail());
      // then
      assertEquals(1, friendsByEmail.size());
    }
    @Test
    public void shouldReturnListWithTwoUsersWhenUserHasTwoFriends() {
      // given
      // when
      List<User> friendsByEmail = userRepository.findFriendsByEmail(user.getEmail());
      List<Long> friendsIdList = friendsByEmail.stream().map(User::getId).toList();
      List<String> friendEmailList = friendsByEmail.stream().map(User::getEmail).toList();
      // then
      assertEquals(2, friendsByEmail.size());
      assertTrue(friendsIdList.containsAll(List.of(user2.getId(), user3.getId())));
      assertTrue(friendEmailList.containsAll(List.of(user2.getEmail(), user3.getEmail())));
    }

    @Test
    public void shouldReturnEmptyListWhenUserDoesNotHaveFriends() {
      // given
      // when
      List<User> friendsByEmail = userRepository.findFriendsByEmail(user4.getEmail());
      // then
      assertEquals(0, friendsByEmail.size());
    }

    @Test
    public void shouldReturnEmptyListWhenEmailIsEmpty() {
      // given
      String emptyEmail = "";
      // when
      List<User> friendsByEmail = userRepository.findFriendsByEmail(emptyEmail);
      // then
      assertTrue(friendsByEmail.isEmpty());
    }

    @Test
    public void shouldReturnEmptyListWhenEmailIsNull() {
      // given
      // when
      List<User> friendsByEmail = userRepository.findFriendsByEmail(null);
      // then
      assertTrue(friendsByEmail.isEmpty());
    }

  }

  private void initUsers(UserGender userGender) {
    user = createTestUser(1L, "Tomasz", "Nowak", "tnowak@byom.de",
      LocalDate.of(1993, 10, 20), userGender, "password",
      LocalDateTime.now().minusDays(10), true);
    user2 = createTestUser(2L, "Igor", "Adamek", "ddwwqq@byom.de",
      LocalDate.of(1994, 11, 21), userGender, "password2",
      LocalDateTime.now().minusDays(10), true);
    userRepository.save(user2);
    user3 = createTestUser(3L, "Wiktor", "Kowalski", "vvaaaccc@byom.de",
      LocalDate.of(1995, 11, 11), userGender, "password3",
      LocalDateTime.now().minusDays(10), true);
    userRepository.save(user3);
    user4 = createTestUser(4L, "Hubert", "Rolski", "aabbcc@byom.de",
      LocalDate.of(1991, 3, 5), userGender, "password4",
      LocalDateTime.now().minusDays(10), true);
    userRepository.save(user4);
    Set<User> userFriends = user.getFriends();
    userFriends.add(user2);
    userFriends.add(user3);
    userRepository.save(user);
    Set<User> user2Friends = user2.getFriends();
    user2Friends.add(user4);
    userRepository.save(user2);
  }

  private UserGender initUserGender() {
    UserGender userGender = new UserGender();
    userGender.setId(1L);
    userGender.setGender(Gender.MAN);
    userGenderRepository.save(userGender);
    return userGender;
  }

  private User createTestUser(Long id, String firstName, String lastName, String email, LocalDate dateOfBirth,
                              UserGender userGender, String password, LocalDateTime joinedAt, boolean isActiveAccount) {
    User user = new User();
    user.setId(id);
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
