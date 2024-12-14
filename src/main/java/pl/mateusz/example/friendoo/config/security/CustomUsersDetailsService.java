package pl.mateusz.example.friendoo.config.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.mateusz.example.friendoo.exceptions.AccountNotActivatedException;
import pl.mateusz.example.friendoo.user.UserCredentialsDto;
import pl.mateusz.example.friendoo.user.UserService;


@SuppressWarnings("checkstyle:MissingJavadocType")
@Service
public class CustomUsersDetailsService implements UserDetailsService {

  private final UserService userService;

  public CustomUsersDetailsService(UserService userService) {
    this.userService = userService;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userService.findCredentialsByEmail(username.toLowerCase())
      .map(this::createUserDetails)
      .orElseThrow(() ->
        new UsernameNotFoundException(String.format("User with email %s not found", username)));
  }

  private UserDetails createUserDetails(UserCredentialsDto credentials) {
    if (!credentials.isActiveAccount()) {
      throw new AccountNotActivatedException("Konto nie aktywowane");
    }
    return User.builder()
      .username(credentials.getEmail())
      .password(credentials.getPassword())
      .roles(credentials.getRoles().toArray(String[]::new))
      .build();

  }
}

