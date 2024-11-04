package pl.mateusz.example.friendoo.user;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;



@SuppressWarnings("checkstyle:MissingJavadocType")
@Controller
public class UserController {

  private final UserService userService;
  private final Logger logger = LoggerFactory.getLogger(UserController.class);

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/home")
  String displayUserHomepage(Authentication authentication, Model model) {
    UserDisplayDto userDisplayDto = userService.findUserToDisplay(authentication.getName())
        .orElseThrow(() -> new UsernameNotFoundException("Brak takiego użytkownika"));
    List<UserDisplayDto> friends = userService.getUserFriendsList(authentication.getName());
    model.addAttribute("user", userDisplayDto);
    model.addAttribute("friends", friends);
    return "news-feed";
  }
}
