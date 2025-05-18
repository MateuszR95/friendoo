package pl.mateusz.example.friendoo.config;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import pl.mateusz.example.friendoo.user.UserDisplayDto;
import pl.mateusz.example.friendoo.user.UserService;

/**
 * Global attributes advice for adding user information to the model.
 */
@ControllerAdvice
public class GlobalAttributesAdvice {

  private final UserService userService;


  public GlobalAttributesAdvice(UserService userService) {
    this.userService = userService;

  }

  /**
   * Adds the current user to the model.
   *
   * @param authentication the authentication object
   * @return the current user
   */
  @ModelAttribute("currentUser")
  public UserDisplayDto getCurrentUser(Authentication authentication) {
    if (authentication == null || !authentication.isAuthenticated()) {
      return null;
    }
    String userEmail = authentication.getName();
    return userService.findUserToDisplay(userEmail)
        .orElseThrow(() -> new UsernameNotFoundException("Brak takiego użytkownika"));
  }

  /**
   * Adds the current user's friends to the model.
   *
   * @param authentication the authentication object
   * @return the list of current user's friends
   */
  @ModelAttribute("friends")
  public List<UserDisplayDto> getFriends(Authentication authentication) {
    if (authentication == null || !authentication.isAuthenticated()) {
      return null;
    }
    String userEmail = authentication.getName();
    return userService.getUserFriendsList(userEmail);
  }

  /**
   * Adds the current user's friends to the model.
   *
   * @param authentication the authentication object
   * @return the set of IDs of the current user's friends
   */
  @ModelAttribute("friendsIds")
  public Set<Long> getUserFriends(Authentication authentication) {
    if (authentication == null || !authentication.isAuthenticated()) {
      return null;
    }
    String userEmail = authentication.getName();
    return userService.getUserFriendsList(userEmail)
      .stream()
      .map(UserDisplayDto::getId)
      .collect(Collectors.toSet());
  }
}
