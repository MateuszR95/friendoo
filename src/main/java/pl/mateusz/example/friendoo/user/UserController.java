package pl.mateusz.example.friendoo.user;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.mateusz.example.friendoo.page.category.PageCategoryDto;
import pl.mateusz.example.friendoo.page.category.PageCategoryService;
import pl.mateusz.example.friendoo.post.user.UserPostDto;
import pl.mateusz.example.friendoo.post.user.UserPostService;
import pl.mateusz.example.friendoo.reaction.user.UserPostReactionService;

/**
 * Controller for user operations.
 */
@Controller
public class UserController {
  private final UserService userService;
  private final PageCategoryService pageCategoryService;
  private final UserPostService userPostService;
  private final UserPostReactionService userPostReactionService;
  private final Logger logger = LoggerFactory.getLogger(UserController.class);

  /**
   * Constructor for UserController.
   *
   * @param userService         the user service
   * @param pageCategoryService the page category service
   * @param userPostService     the user post service
   */
  public UserController(UserService userService, PageCategoryService pageCategoryService,
                        UserPostService userPostService,
                        UserPostReactionService userPostReactionService) {
    this.userService = userService;
    this.pageCategoryService = pageCategoryService;
    this.userPostService = userPostService;
    this.userPostReactionService = userPostReactionService;
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
    TreeSet<PageCategoryDto> allPageCategories = pageCategoryService.getAllPageCategories();
    model.addAttribute("user", userAdditionalDetailsDto);
    model.addAttribute("pageCategories", allPageCategories);
    return "complete-user-profile";
  }

  @PostMapping("/complete-profile")
  String completeUserDetails(@Valid @ModelAttribute("user") UserAdditionalDetailsDto
                               userAdditionalDetailsDto, BindingResult bindingResult, Model model) {
    if (bindingResult.hasErrors()) {
      TreeSet<PageCategoryDto> allPageCategories = pageCategoryService.getAllPageCategories();
      model.addAttribute("pageCategories", allPageCategories);
      return "complete-user-profile";
    }
    userService.completeUserProfileDetails(userAdditionalDetailsDto);
    return "redirect:/home";
  }

  @GetMapping("/{firstName}.{lastName}.{id}")
  String displayUserProfilePage(@PathVariable String firstName, @PathVariable String lastName,
                                @PathVariable Long id, Model model) {
    Optional<UserDisplayDto> userDisplayDtoOptional = userService.findUserByNameAndId(
        firstName, lastName, id);
    if (userDisplayDtoOptional.isEmpty()) {
      logger.warn("Użytkownik {} {} o numerze id {} nie znaleziony", firstName, lastName, id);
      return "error/404";
    }
    model.addAttribute("user", userDisplayDtoOptional.get());
    return "user-profile-page";
  }

  @GetMapping("/{firstName}.{lastName}.{id}/activity")
  String displayUserActivity(@PathVariable String firstName, @PathVariable String lastName,
                             @PathVariable Long id, Model model, Authentication authentication) {
    Optional<UserDisplayDto> userDisplayDtoOptional = userService.findUserByNameAndId(
        firstName, lastName, id);
    if (userDisplayDtoOptional.isEmpty()) {
      logger.warn("Użytkownik {} {} o numerze id {} nie znaleziony", firstName, lastName, id);
      return "error/404";
    }
    List<UserPostDto> userPostsByAuthorId = userPostService.getUserPostsByAuthorId(id);
    List<Long> currentLoggedUserFriendsIds = userService
        .getCurrentLoggedUserFriendsIds(authentication);
    UserCredentialsDto currentLoggedUser = userService.findCredentialsByEmail(authentication
        .getName()).orElseThrow(() -> new UsernameNotFoundException("Brak takiego użytkownika"));
    model.addAttribute("currentLoggedUserFriendsIds", currentLoggedUserFriendsIds);
    model.addAttribute("posts", userPostsByAuthorId);
    model.addAttribute("user", userDisplayDtoOptional.get());
    model.addAttribute("currentLoggedUser", currentLoggedUser);
    return "user-activity";
  }
}