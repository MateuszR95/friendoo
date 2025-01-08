package pl.mateusz.example.friendoo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pl.mateusz.example.friendoo.config.security.CustomUsersDetailsService;
import pl.mateusz.example.friendoo.user.UserCredentialsDto;
import pl.mateusz.example.friendoo.user.UserService;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomUsersDetailsServiceTests {

  @Mock
  private UserService userService;

  @InjectMocks
  private CustomUsersDetailsService customUsersDetailsService;

  @Nested
  class loadUserByUsernameTests {

    private UserCredentialsDto userCredentialsDto;

    @BeforeEach
    void setup() {
      userCredentialsDto = UserCredentialsDto.builder()
        .email("example@byom.de")
        .isActiveAccount(true)
        .password("password")
        .roles(Set.of("USER"))
        .build();
    }

    @Test
    public void shouldLoadUserByUsernameAndReturnUserDetailsObject() {
      // given
      // when
      when(userService.findCredentialsByEmail(userCredentialsDto.getEmail()))
        .thenReturn(Optional.of(userCredentialsDto));
      UserDetails result = customUsersDetailsService.loadUserByUsername(userCredentialsDto.getEmail());
      // then
      assertNotNull(result);
      assertInstanceOf(UserDetails.class, result);
      assertEquals(result.getUsername(), userCredentialsDto.getEmail());
      assertEquals(result.getPassword(), userCredentialsDto.getPassword());
      assertTrue(result.getAuthorities().stream().anyMatch(authority -> authority.getAuthority()
        .equals("ROLE_USER")));
      verify(userService, times(1))
        .findCredentialsByEmail(userCredentialsDto.getEmail());

    }

    @Test
    public void shouldThrowUsernameNotFoundExceptionWhenUserDoesNotExist() {
      // given
      String notExistingEmail = "invalid@byom.de";
      // when
      when(userService.findCredentialsByEmail(notExistingEmail))
        .thenReturn(Optional.empty());
      // then
      assertThrows(UsernameNotFoundException.class,
        () -> customUsersDetailsService.loadUserByUsername(notExistingEmail));
      verify(userService, times(1))
        .findCredentialsByEmail(notExistingEmail);

    }

    @Test
    public void shouldLoadUserByUsernameAndReturnUserDetailsObjectWhenUsernameIsCaseInsensitive() {
      // given
      String userEmailToUppercase = userCredentialsDto.getEmail().toUpperCase();
      String normalizedEmail = userEmailToUppercase.toLowerCase();
      // when
      when(userService.findCredentialsByEmail(normalizedEmail))
        .thenReturn(Optional.of(userCredentialsDto));
      UserDetails result = customUsersDetailsService.loadUserByUsername(userEmailToUppercase);
      // then
      assertNotNull(result);
      assertInstanceOf(UserDetails.class, result);
      assertEquals(result.getUsername(), userCredentialsDto.getEmail());
      assertEquals(result.getPassword(), userCredentialsDto.getPassword());
      assertTrue(result.getAuthorities().stream().anyMatch(authority -> authority.getAuthority()
        .equals("ROLE_USER")));
      verify(userService, times(1))
        .findCredentialsByEmail(userCredentialsDto.getEmail());

    }


  }
}
