package pl.mateusz.example.friendoo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.mateusz.example.friendoo.gender.Gender;
import pl.mateusz.example.friendoo.gender.UserGender;
import pl.mateusz.example.friendoo.user.User;
import pl.mateusz.example.friendoo.user.activation.UserActivationToken;
import pl.mateusz.example.friendoo.user.activation.UserActivationTokenDto;
import pl.mateusz.example.friendoo.user.activation.UserActivationTokenRepository;
import pl.mateusz.example.friendoo.user.activation.UserActivationTokenService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserActivationTokenServiceTests {

  @Mock
  private UserActivationTokenRepository userActivationTokenRepository;

  @InjectMocks
  private UserActivationTokenService userActivationTokenService;

  @Nested
  class generateAndSaveTokenTests {

    private User testUser;
    private static final int EXPIRATION_TIME_IN_MINUTES = 15;

    @BeforeEach
      void setup() {
      testUser = createTestUser(1L, "Tomasz", "Nowak", "example@byom.de",
        LocalDate.of(1993, 10, 20), Gender.MAN, "test", LocalDateTime.now().minusDays(10),
        false);
    }

    @Test
    public void shouldGenerateAndSaveTokenSuccessfully() {
      // given
      // when
      when(userActivationTokenRepository.save(any(UserActivationToken.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));
      UserActivationToken result = userActivationTokenService.generateAndSaveToken(testUser);
      // then
      assertNotNull(result);
      assertEquals(result.getUser(), testUser);
      assertEquals(result.getCreationDate(), testUser.getJoinedAt());
      assertEquals(result.getExpireDate(), testUser.getJoinedAt().plusMinutes(EXPIRATION_TIME_IN_MINUTES));
      assertInstanceOf(UserActivationToken.class, result);
      verify(userActivationTokenRepository,
        times(1)).save(any(UserActivationToken.class));
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenUserIsNull() {
      // given
      testUser = null;
      // when & then
      assertThrows(NullPointerException.class,
        () -> userActivationTokenService.generateAndSaveToken(testUser));
      verify(userActivationTokenRepository,
        times(0)).save(any(UserActivationToken.class));
    }

    @Test
    public void shouldReturnNullWhenUserActivationTokenIsNotSaved() {
      // given
      // when
      when(userActivationTokenRepository.save(any(UserActivationToken.class))).thenReturn(null);
      UserActivationToken result = userActivationTokenService.generateAndSaveToken(testUser);
      // then
      assertNull(result);
      verify(userActivationTokenRepository,
        times(1)).save(any(UserActivationToken.class));
    }
  }

  @Nested
  class getUserActivationTokenByUserEmailTests{

    private User testUser;
    private UserActivationToken userActivationToken;

    private static final int EXPIRATION_TIME_IN_MINUTES = 15;
    @BeforeEach
    void setup() {
      testUser = createTestUser(1L, "Tomasz", "Nowak", "example@byom.de",
        LocalDate.of(1993, 10, 20), Gender.MAN, "test", LocalDateTime.now().minusDays(10),
        false);
      userActivationToken = createUserActivationToken(1L, "abcdefgh", testUser.getJoinedAt(),
        testUser.getJoinedAt().plusMinutes(EXPIRATION_TIME_IN_MINUTES), testUser);
    }
    @Test
    void shouldReturnUserActivationTokenDtoByUserEmail() {
      // given
      // when
      when(userActivationTokenRepository.getUserActivationTokenByUserEmail(testUser.getEmail()))
        .thenReturn(Optional.of(userActivationToken));
      Optional<UserActivationTokenDto> result =
        userActivationTokenService.getUserActivationTokenByUserEmail(testUser.getEmail());
      // then
      assertTrue(result.isPresent());
      assertEquals(result.get().getUserEmail(), testUser.getEmail());
      assertEquals(userActivationToken.getToken(), result.get().getToken());
      assertInstanceOf(UserActivationTokenDto.class, result.get());
      verify(userActivationTokenRepository,
        times(1)).getUserActivationTokenByUserEmail(testUser.getEmail());

    }

    @Test
    void shouldReturnEmptyOptionalWhenUserActivationTokenDoesNotExist() {
      // given
      // when
      when(userActivationTokenRepository.getUserActivationTokenByUserEmail(testUser.getEmail()))
        .thenReturn(Optional.empty());
      Optional<UserActivationTokenDto> result =
        userActivationTokenService.getUserActivationTokenByUserEmail(testUser.getEmail());
      // then
      assertTrue(result.isEmpty());
      verify(userActivationTokenRepository,
        times(1)).getUserActivationTokenByUserEmail(testUser.getEmail());
    }
  }

  private User createTestUser(Long id, String firstName, String lastName, String email, LocalDate dateOfBirth,
                              Gender gender, String password, LocalDateTime joinedAt, boolean isActiveAccount) {
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

  private UserActivationToken createUserActivationToken(Long id, String token, LocalDateTime creationDate,
                                                        LocalDateTime expireDate, User user) {
    UserActivationToken userActivationToken = new UserActivationToken();
    userActivationToken.setId(id);
    userActivationToken.setToken(token);
    userActivationToken.setCreationDate(creationDate);
    userActivationToken.setExpireDate(expireDate);
    userActivationToken.setUser(user);
    return userActivationToken;

  }


}
