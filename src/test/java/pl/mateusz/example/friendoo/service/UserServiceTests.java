package pl.mateusz.example.friendoo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSendException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.mateusz.example.friendoo.email.MailService;
import pl.mateusz.example.friendoo.exceptions.*;
import pl.mateusz.example.friendoo.gender.Gender;
import pl.mateusz.example.friendoo.gender.UserGender;
import pl.mateusz.example.friendoo.gender.UserGenderRepository;
import pl.mateusz.example.friendoo.user.*;
import pl.mateusz.example.friendoo.user.activation.UserActivationToken;
import pl.mateusz.example.friendoo.user.activation.UserActivationTokenDto;
import pl.mateusz.example.friendoo.user.activation.UserActivationTokenService;
import pl.mateusz.example.friendoo.user.passwordreset.UserPasswordResetDto;
import pl.mateusz.example.friendoo.user.passwordreset.UserPasswordResetTokenService;
import pl.mateusz.example.friendoo.user.registration.UserRegistrationDto;
import pl.mateusz.example.friendoo.user.role.Role;
import pl.mateusz.example.friendoo.user.role.UserRole;
import pl.mateusz.example.friendoo.user.role.UserRoleRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserGenderRepository userGenderRepository;

  @Mock
  private UserRoleRepository userRoleRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private MailService mailService;

  @Mock
  private UserActivationTokenService userActivationTokenService;

  @Mock
  private UserPasswordResetTokenService userPasswordResetTokenService;

  @InjectMocks
  private UserService userService;
  @Nested
  class findCredentialsByEmailTests {
    private User userTest;
    @BeforeEach
    void setup() {
      userTest = createTestUser(1L, "Tomasz", "Nowak", "tnowak@byom.de",
        LocalDate.of(1993, 10, 20), Gender.MAN, "test", LocalDateTime.now().minusDays(10),
        true);

    }
    @Test
    public void shouldFindCredentialsByEmailWhenUserExists() {
      //given
      when(userRepository.findUserByEmail(userTest.getEmail())).thenReturn(Optional.of(userTest));
      //when
      Optional<UserCredentialsDto> result = userService.findCredentialsByEmail(userTest.getEmail());
      //then
      assertAll(
        () -> assertTrue(result.isPresent()),
        () -> assertEquals(result.get().getEmail(), userTest.getEmail()),
        () -> assertEquals(result.get().getPassword(), userTest.getPassword())
      );

    }

    @Test
    public void shouldNotFindCredentialsByEmailWhenUserDoesNotExist() {
      //given
      when(userRepository.findUserByEmail(userTest.getEmail())).thenReturn(Optional.empty());
      //when
      Optional<UserCredentialsDto> result = userService.findCredentialsByEmail(userTest.getEmail());
      //then
      assertTrue(result.isEmpty());
    }

    @Test
    public void shouldReturnOptionalWithUserCredentialsDtoWhenEmailExists() {
      //given
      when(userRepository.findUserByEmail(userTest.getEmail())).thenReturn(Optional.of(userTest));
      //when
      Optional<UserCredentialsDto> result = userService.findCredentialsByEmail(userTest.getEmail());
      //then
      assertAll(
        () -> assertTrue(result.isPresent()),
        () -> assertInstanceOf(UserCredentialsDto.class, result.get())
      );

    }

  }
  @Nested
  class findUserToDisplayTests {
    private User userTest;
    @BeforeEach
    void setup() {
      userTest = createTestUser(1L, "Tomasz", "Nowak", "tnowak@byom.de",
        LocalDate.of(1993, 10, 20), Gender.MAN, "test", LocalDateTime.now().minusDays(10),
        true);
    }
    @Test
    void shouldFindUserToDisplayWhenUserExists() {
      //given
      when(userRepository.findUserByEmail(userTest.getEmail())).thenReturn(Optional.of(userTest));
      //when
      Optional<UserDisplayDto> result = userService.findUserToDisplay(userTest.getEmail());
      //then
      assertAll(
        () -> assertTrue(result.isPresent()),
        () -> assertEquals(result.get().getFirstName(), userTest.getFirstName()),
        () -> assertEquals(result.get().getLastName(), userTest.getLastName())
      );
    }

    @Test
    void shouldNotFindUserToDisplayWhenUserDoesNotExist() {
      //given
      when(userRepository.findUserByEmail(userTest.getEmail())).thenReturn(Optional.empty());
      //when
      Optional<UserDisplayDto> result = userService.findUserToDisplay(userTest.getEmail());
      //then
      assertTrue(result.isEmpty());
    }

    @Test
    public void shouldReturnOptionalWithUserDisplayDtoWhenUserExists() {
      //given
      when(userRepository.findUserByEmail(userTest.getEmail())).thenReturn(Optional.of(userTest));
      //when
      Optional<UserDisplayDto> result = userService.findUserToDisplay(userTest.getEmail());
      //then
      assertAll(
        () -> assertTrue(result.isPresent()),
        () -> assertInstanceOf(UserDisplayDto.class, result.get())
      );

    }

  }
  @Nested
  class getUserFriendsListTests {

    @Test
    public void shouldReturnEmptyListWhenUserDoesNotExist() {
      // given
      String email = "nonexistent@byom.de";
      when(userRepository.findFriendsByUserEmail(email)).thenReturn(Collections.emptyList());
      // when
      List<UserDisplayDto> userFriendsList = userService.getUserFriendsList(email);
      // then
      assertTrue(userFriendsList.isEmpty());
    }

    @Test
    public void shouldReturnEmptyListWhenUserHasNoFriends() {
      // given
      String userEmail = "usertest@byom.de";
      when(userRepository.findFriendsByUserEmail(userEmail)).thenReturn(Collections.emptyList());
      // when
      List<UserDisplayDto> userFriendsList = userService.getUserFriendsList(userEmail);
      // then
      assertTrue(userFriendsList.isEmpty());
    }

    @Test
    public void shouldReturnTwoFriendsWhenUserHasTwoFriends() {
      // given
      String userEmail = "usertest@byom.de";
      User userFriend1 = createTestUser(2L, "Adam", "Nowak", "anowak@byom.de",
        LocalDate.of(1993, 11, 11), Gender.MAN, "test", LocalDateTime.now().minusDays(5),
        true);
      User userFriend2 = createTestUser(3L, "Anna", "Kowalska", "akowalska@byom.de",
        LocalDate.of(1999, 9, 21), Gender.WOMAN, "test", LocalDateTime.now().minusDays(3),
        true);
      when(userRepository.findFriendsByUserEmail(userEmail)).thenReturn(List.of(userFriend1, userFriend2));
      // when
      List<UserDisplayDto> userFriendsList = userService.getUserFriendsList(userEmail);
      // then
      assertAll(
        () -> assertFalse(userFriendsList.isEmpty()),
        () -> assertEquals(2, userFriendsList.size())
      );
    }

    @Test
    public void shouldReturnListWithUserDisplayDtoWhenUserHasFriends() {
      // given
      String userEmail = "usertest@byom.de";
      User userFriend1 = createTestUser(2L, "Adam", "Nowak", "anowak@byom.de",
        LocalDate.of(1993, 11, 11), Gender.MAN, "test", LocalDateTime.now().minusDays(5),
        true);
      when(userRepository.findFriendsByUserEmail(userEmail)).thenReturn(List.of(userFriend1));
      // when
      List<UserDisplayDto> userFriendsList = userService.getUserFriendsList(userEmail);
      // then
      assertInstanceOf(UserDisplayDto.class, userFriendsList.get(0));
    }
  }

  @Nested
  class registerAccountTests {

    private User user;
    private UserRegistrationDto userRegistrationDto;
    private UserActivationToken userActivationToken;
    @BeforeEach
    void setUp() {
      userRegistrationDto = UserRegistrationDto.builder()
        .firstName("Adam")
        .lastName("Nowak")
        .email("anowak@byom.de")
        .dateOfBirth(LocalDate.of(1993, 11, 11))
        .gender(Gender.MAN.name())
        .password("test")
        .repeatedPassword("test")
        .build();

      user = createTestUser(2L, "Adam", "Nowak", "anowak@byom.de",
        LocalDate.of(1993, 11, 11), Gender.MAN, "test", LocalDateTime.now().minusDays(5),
        true);

      userActivationToken = createUserActivationToken(1L, user,
        "tokencode", LocalDateTime.now(),
        LocalDateTime.now().plusMinutes(15));
    }

    @Test
    void shouldRegisterAccountSuccessfully() {
      // given
      UserGender userGender = new UserGender();
      userGender.setGender(Gender.MAN);
      when(userGenderRepository.getUserGenderByGender(Gender.MAN)).thenReturn(Optional.of(userGender));

      UserRole userRole = new UserRole();
      userRole.setRole(Role.USER);
      when(userRoleRepository.getUserRoleByRole(Role.USER)).thenReturn(Optional.of(userRole));
      // when
      when(userRepository.save(any(User.class))).thenReturn(user);
      when(userActivationTokenService.generateAndSaveToken(any(User.class))).thenReturn(userActivationToken);
      when(mailService.sendActivationCode(user.getEmail(), user.getFirstName(), userActivationToken.getToken()))
        .thenReturn(true);

      // then
      userService.registerAccount(userRegistrationDto);
      ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
      verify(userRepository, times(1)).save(userCaptor.capture());
      verify(mailService, times(1)).sendActivationCode(
        user.getEmail(), user.getFirstName(), userActivationToken.getToken()
      );
      User savedUser = userCaptor.getValue();
      assertEquals(user.getEmail(), savedUser.getEmail());
      assertEquals(user.getFirstName(), savedUser.getFirstName());
      assertEquals(user.getLastName(), savedUser.getLastName());
      assertEquals(passwordEncoder.encode(user.getPassword()), savedUser.getPassword());
    }

    @Test
    void shouldNotRegisterAccountWhenUserAlreadyExists() {
      // given
      // when
      when(userRepository.existsUserByEmail(userRegistrationDto.getEmail())).thenReturn(true);
      // then
      assertThrows(UserValidationException.class, () -> userService.registerAccount(userRegistrationDto));
      verify(userRepository, never()).save(any(User.class));
      verify(mailService, never()).sendActivationCode(anyString(), anyString(), anyString());
    }

    @Test
    void shouldNotRegisterAccountWhenUserEmailWithActivationCodeCannotBeSent() {
      // given
      UserGender userGender = new UserGender();
      userGender.setGender(Gender.MAN);
      when(userGenderRepository.getUserGenderByGender(Gender.MAN)).thenReturn(Optional.of(userGender));
      UserRole userRole = new UserRole();
      userRole.setRole(Role.USER);
      // when
      when(userRoleRepository.getUserRoleByRole(Role.USER)).thenReturn(Optional.of(userRole));
      when(userRepository.save(any(User.class))).thenReturn(user);
      when(userActivationTokenService.generateAndSaveToken(any(User.class))).thenReturn(userActivationToken);
      when(userRepository.existsUserByEmail(userRegistrationDto.getEmail())).thenReturn(false);
      when(mailService.sendActivationCode(anyString(), anyString(), anyString())).thenReturn(false);
      //  then
      assertThrows(MailSendException.class, () -> userService.registerAccount(userRegistrationDto));
      verify(userRepository, times(1)).save(any(User.class));
      verify(mailService, times(1)).sendActivationCode(
        user.getEmail(), user.getFirstName(), userActivationToken.getToken()
      );

    }
  }

  @Nested
  class activateAccountTests {

    private final String validEmail = "user@example.com";
    private final String validToken = "validToken";
    private final String invalidToken = "invalidToken";
    private User user;
    private UserActivationTokenDto userActivationTokenDto;
    @BeforeEach
    void setUp() {
      user = createTestUser(2L, "Adam", "Nowak", "user@example.com",
        LocalDate.of(1993, 11, 11), Gender.MAN, "test", LocalDateTime.now().minusDays(5),
        false);
      userActivationTokenDto = new UserActivationTokenDto();
      userActivationTokenDto.setUserEmail(validEmail);
      userActivationTokenDto.setToken(validToken);
    }

    @Test
    public void shouldActivateAccountSuccessfully() {
      // given
      // when
      when(userActivationTokenService.getUserActivationTokenByUserEmail(anyString())).thenReturn(Optional.of(userActivationTokenDto));
      when(userRepository.findUserByEmail(validEmail)).thenReturn(Optional.of(user));
      user.setActiveAccount(false);
      userService.activateAccount(validEmail, validToken);
      // then
      verify(userRepository, times(1)).save(user);
      assertTrue(user.isActiveAccount());
      verify(userActivationTokenService, times(1)).deleteTokenByUserEmail(validEmail);
    }
    @Test
    public void shouldNotActivateAccountWhenTokenIsExpired() {
      // given
      // when
      when(userActivationTokenService.getUserActivationTokenByUserEmail(anyString())).thenReturn(Optional.empty());
      // then
      assertThrows(ExpiredActivationTokenException.class, () -> userService.activateAccount(validEmail, validToken));
      assertFalse(user.isActiveAccount());
      verify(userRepository, times(0)).save(user);
      verify(userActivationTokenService, times(0)).deleteTokenByUserEmail(user.getEmail());
    }

    @Test
    public void shouldNotActivateAccountWhenEnteredTokenIsIncorrect() {
      // given
      // when
      when(userActivationTokenService.getUserActivationTokenByUserEmail(anyString())).thenReturn(Optional.of(userActivationTokenDto));
      // then
      assertThrows(InvalidTokenException.class, () -> userService.activateAccount(validEmail, invalidToken));
      assertFalse(user.isActiveAccount());
      verify(userRepository, times(0)).save(user);
      verify(userActivationTokenService, times(0)).deleteTokenByUserEmail(user.getEmail());
    }

    @Test
    public void shouldNotActivateAccountWhenUserIsAlreadyActivated() {
      // given
      // when
      when(userActivationTokenService.getUserActivationTokenByUserEmail(anyString())).thenReturn(Optional.of(userActivationTokenDto));
      when(userRepository.findUserByEmail(validEmail)).thenReturn(Optional.of(user));
      user.setActiveAccount(true);
      // then
      assertThrows(AccountAlreadyActivatedException.class, () -> userService.activateAccount(validEmail, validToken));
      assertTrue(user.isActiveAccount());
      verify(userRepository, times(0)).save(user);
      verify(userActivationTokenService, times(0)).deleteTokenByUserEmail(user.getEmail());
    }
  }

  @Nested
  class isAccountActivatedTests {
    @Test
    public void shouldReturnTrueWhenUserIsActivated() {
      // given
      User testUser = createTestUser(1L, "Tomasz", "Nowak", "tnowak@byom.de",
        LocalDate.of(1993, 10, 20), Gender.MAN, "test", LocalDateTime.now().minusDays(10),
        true);
      // when
      when(userRepository.findUserByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));
      boolean isActivated = userService.isAccountActivated(testUser.getEmail());
      // then
      assertTrue(isActivated);
      verify(userRepository).findUserByEmail(testUser.getEmail());
    }

    @Test
    public void shouldReturnFalseWhenUserIsNotActivated() {
      // given
      User testUser = createTestUser(1L, "Tomasz", "Nowak", "tnowak@byom.de",
        LocalDate.of(1993, 10, 20), Gender.MAN, "test", LocalDateTime.now().minusDays(10),
        false);
      // when
      when(userRepository.findUserByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));
      boolean isActivated = userService.isAccountActivated(testUser.getEmail());
      // then
      assertFalse(isActivated);
      verify(userRepository).findUserByEmail(testUser.getEmail());
    }

    @Test
    public void shouldThrowExceptionWhenUserDoesNotExist() {
      // given
      String exampleEmail = "example@byom.de";
      // when
      when(userRepository.findUserByEmail(exampleEmail)).thenReturn(Optional.empty());
      // then
      assertThrows(UserNotFoundException.class, () -> userService.isAccountActivated(exampleEmail));
      verify(userRepository).findUserByEmail(exampleEmail);
    }

  }

  @Nested
  class sendPasswordResetLinkTests {

    @Test
    public void shouldSuccessfullySendPasswordResetLink() {
      // given
      String exampleEmail = "test@byom.de";
      // when
      doNothing().when(userPasswordResetTokenService).createAndSaveUserPasswordResetToken(exampleEmail);
      when(mailService.sendPasswordResetEmail(exampleEmail)).thenReturn(true);
      userService.sendPasswordResetLink(exampleEmail);
      // then
      verify(userPasswordResetTokenService, times(1)).createAndSaveUserPasswordResetToken(exampleEmail);
      verify(mailService, times(1)).sendPasswordResetEmail(exampleEmail);
    }

    @Test
    public void shouldThrowMailSendingExceptionWhenPasswordResetEmailIsNotSent() {
      // given
      String exampleEmail = "test@byom.de";
      // when
      doNothing().when(userPasswordResetTokenService).createAndSaveUserPasswordResetToken(exampleEmail);
      when(mailService.sendPasswordResetEmail(exampleEmail)).thenReturn(false);
      // then
      assertThrows(MailSendingException.class, () -> userService.sendPasswordResetLink(exampleEmail));
      verify(userPasswordResetTokenService, times(1)).createAndSaveUserPasswordResetToken(exampleEmail);
      verify(mailService, times(1)).sendPasswordResetEmail(exampleEmail);
    }
  }

  @Nested
  class getUserPasswordResetDtoByEmailTests {
    @Test
    public void shouldReturnOptionalWithUserPasswordResetDtoWhenUserExists() {
      // given
      User testUser = createTestUser(1L, "Tomasz", "Nowak", "example@byom.de",
        LocalDate.of(1993, 10, 20), Gender.MAN, "test", LocalDateTime.now().minusDays(10),
        false);
      // when
      when(userRepository.findUserByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));
      Optional<UserPasswordResetDto> userPasswordResetDtoByEmail = userService.getUserPasswordResetDtoByEmail(testUser.getEmail());
      // then
      assertTrue(userPasswordResetDtoByEmail.isPresent());
      assertEquals(testUser.getEmail(), userPasswordResetDtoByEmail.get().getEmail());
      verify(userRepository, times(1)).findUserByEmail(testUser.getEmail());
    }

    @Test
    public void shouldReturnEmptyOptionalWhenUserDoesNotExist() {
      // given
      String nonExistentEmail = "testemail@byom.de";
      // when
      when(userRepository.findUserByEmail(nonExistentEmail)).thenReturn(Optional.empty());
      Optional<UserPasswordResetDto> userPasswordResetDtoByEmail = userService.getUserPasswordResetDtoByEmail(nonExistentEmail);
      // then
      assertFalse(userPasswordResetDtoByEmail.isPresent());
      verify(userRepository, times(1)).findUserByEmail(nonExistentEmail);
    }

  }

  @Nested
  class resetPasswordTests {
    @Test
    public void shouldSuccessfullyResetPassword() {
      // given
      UserPasswordResetDto passwordResetDto = UserPasswordResetDto.builder()
        .email("tnowak@byom.de")
        .password("change")
        .repeatedPassword("change")
        .build();
      User testUser = createTestUser(1L, "Tomasz", "Nowak", "tnowak@byom.de",
        LocalDate.of(1993, 10, 20), Gender.MAN, "pass", LocalDateTime.now().minusDays(10),
        true);
      // when
      when(userRepository.findUserByEmail(passwordResetDto.getEmail())).thenReturn(Optional.of(testUser));
      testUser.setPassword(passwordEncoder.encode(passwordResetDto.getPassword()));
      when(userRepository.save(testUser)).thenReturn(testUser);
      boolean result = userService.resetPassword(passwordResetDto);
      // then
      assertTrue(result);
      assertEquals(passwordEncoder.encode(testUser.getPassword()), passwordEncoder.encode(passwordResetDto.getPassword()));
      verify(userRepository, times(1)).findUserByEmail(passwordResetDto.getEmail());
      verify(userRepository, times(1)).save(testUser);
    }


    @Test
    public void shouldThrowUsernameNotFoundExceptionWhenUserWithSpecifiedEmailDoesNotExist() {
      // given
      UserPasswordResetDto passwordResetDto = UserPasswordResetDto.builder()
        .email("tnowak@byom.de")
        .password("change")
        .repeatedPassword("change")
        .build();
      // when
      when(userRepository.findUserByEmail(passwordResetDto.getEmail())).thenReturn(Optional.empty());
      // then
      assertThrows(UsernameNotFoundException.class, () -> userService.resetPassword(passwordResetDto));
      verify(userRepository, times(1)).findUserByEmail(passwordResetDto.getEmail());
    }

    @Test
    public void shouldNotResetPasswordWhereNewPasswordsDoNotMatch() {
      // given
      UserPasswordResetDto passwordResetDto = UserPasswordResetDto.builder()
        .email("tnowak@byom.de")
        .password("change")
        .repeatedPassword("pass")
        .build();
      User testUser = createTestUser(1L, "Tomasz", "Nowak", "tnowak@byom.de",
        LocalDate.of(1993, 10, 20), Gender.MAN, "password", LocalDateTime.now().minusDays(10),
        true);
      // when
      when(userRepository.findUserByEmail(passwordResetDto.getEmail())).thenReturn(Optional.of(testUser));
      boolean result = userService.resetPassword(passwordResetDto);
      // then
      assertFalse(result);
      assertEquals(passwordEncoder.encode(testUser.getPassword()), passwordEncoder.encode(passwordResetDto.getPassword()));
      verify(userRepository, times(1)).findUserByEmail(passwordResetDto.getEmail());
      verify(userRepository, times(0)).save(testUser);
    }
  }


  private User createTestUser(Long id, String firstName, String lastName, String email, LocalDate dateOfBirth,
                              Gender  gender, String password, LocalDateTime joinedAt, boolean isActiveAccount) {
    User user = new User();
    user.setId(id);
    user.setFirstName(firstName);
    user.setLastName(lastName);
    user.setEmail(email);
    user.setDateOfBirth(dateOfBirth);
    UserGender userGender = new UserGender();
    userGender.setGender(gender);
    user.setPassword(password);
    user.setJoinedAt(joinedAt);
    user.setActiveAccount(isActiveAccount);
    return user;
  }

  private UserActivationToken createUserActivationToken(Long id, User user, String token,
                                                        LocalDateTime creationDate, LocalDateTime expireDate) {
    UserActivationToken userActivationToken = new UserActivationToken();
    userActivationToken.setId(id);
    userActivationToken.setUser(user);
    userActivationToken.setToken(token);
    userActivationToken.setCreationDate(creationDate);
    userActivationToken.setExpireDate(expireDate);
    return userActivationToken;
  }

}
