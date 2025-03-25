package pl.mateusz.example.friendoo.user;

import jakarta.validation.Valid;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@SuppressWarnings("checkstyle:MissingJavadocType")
@Controller
public class UserController {
  private final UserService userService;
  private final Logger logger = LoggerFactory.getLogger(UserController.class);

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/home")
  String displayUserHomepage(Authentication authentication, Model model,
                             RedirectAttributes redirectAttributes) {
    String userEmail = authentication.getName();
    if (!userService.isUserProfileCompleted(userEmail)) {
      UserAdditionalDetailsDto userToCompleteProfile = userService
          .findUserToCompleteProfile(userEmail);
      redirectAttributes.addFlashAttribute("user", userToCompleteProfile);
      return "redirect:/complete-profile";
    }
    UserDisplayDto userDisplayDto = userService.findUserToDisplay(authentication.getName())
        .orElseThrow(() -> new UsernameNotFoundException("Brak takiego użytkownika"));
    List<UserDisplayDto> friends = userService.getUserFriendsList(authentication.getName());
    model.addAttribute("user", userDisplayDto);
    model.addAttribute("friends", friends);
    return "news-feed";
  }

  @GetMapping("/complete-profile")
  String displayUserDetailsForm(Model model, @ModelAttribute("user")
      UserAdditionalDetailsDto userAdditionalDetailsDto) {
    model.addAttribute("user", userAdditionalDetailsDto);
    return "complete-user-profile";
  }

  @PostMapping("/complete-profile")
  String completeUserDetails(@Valid @ModelAttribute("user") UserAdditionalDetailsDto
                               userAdditionalDetailsDto, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return "complete-user-profile";
    }
    userService.completeUserProfileDetails(userAdditionalDetailsDto);
    return "redirect:/home";
  }
}
