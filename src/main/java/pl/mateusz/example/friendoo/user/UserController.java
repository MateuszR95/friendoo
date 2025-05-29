package pl.mateusz.example.friendoo.user;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.mateusz.example.friendoo.comment.PostCommentAuthorDto;
import pl.mateusz.example.friendoo.exceptions.UserNotFoundException;
import pl.mateusz.example.friendoo.invitation.InvitationToFriendshipService;
import pl.mateusz.example.friendoo.page.category.PageCategoryDto;
import pl.mateusz.example.friendoo.page.category.PageCategoryService;
import pl.mateusz.example.friendoo.post.PostDto;
import pl.mateusz.example.friendoo.post.PostService;
import pl.mateusz.example.friendoo.post.postversion.PostVersionService;
import pl.mateusz.example.friendoo.visit.UserProfileVisitService;

/**
 * Controller for user operations.
 */
@Controller
public class UserController {
  private final UserService userService;
  private final PageCategoryService pageCategoryService;
  private final PostService postService;
  private final PostVersionService postVersionService;

  private final UserProfileVisitService userProfileVisitService;

  private final InvitationToFriendshipService invitationToFriendshipService;
  private final Logger logger = LoggerFactory.getLogger(UserController.class);

  /**
   * Constructor for UserController.
   *
   * @param userService            the user service
   * @param pageCategoryService    the page category service
   * @param postService            the post service
   * @param postVersionService     the post version service
   * @param userProfileVisitService the user profile visit service
   */
  public UserController(UserService userService, PageCategoryService pageCategoryService,
                        PostService postService, PostVersionService postVersionService,
                        UserProfileVisitService userProfileVisitService,
                        InvitationToFriendshipService invitationToFriendshipService) {
    this.userService = userService;
    this.pageCategoryService = pageCategoryService;
    this.postService = postService;
    this.postVersionService = postVersionService;
    this.userProfileVisitService = userProfileVisitService;
    this.invitationToFriendshipService = invitationToFriendshipService;
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
    List<UserDisplayDto> friends = userService.getUserFriendsListByUserEmail(
        authentication.getName());
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
                                @PathVariable Long id, Authentication authentication,
                                Model model) {
    Optional<UserDisplayDto> userDisplayDtoOptional = userService.findUserByNameAndId(
        firstName, lastName, id);
    if (userDisplayDtoOptional.isEmpty()) {
      logger.warn("Użytkownik {} {} o numerze id {} nie znaleziony", firstName, lastName, id);
      return "error/404";
    }
    try {
      userProfileVisitService.saveUserProfileVisit(id, authentication);
    } catch (UserNotFoundException ex) {
      logger.warn("Nie udało się zapisać wizyty zalogowanego użytkownika na"
          + "  profilu użytkownika o id {}", id);
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
    List<PostDto> userPostsByAuthorId = postService.getUserPostsByAuthorId(id);
    List<Long> currentLoggedUserFriendsIds = userService
        .getCurrentLoggedUserFriendsIds(authentication);
    UserCredentialsDto currentLoggedUser = userService.findCredentialsByEmail(authentication
        .getName()).orElseThrow(() -> new UsernameNotFoundException("Brak takiego użytkownika"));
    Map<Long, List<PostCommentAuthorDto>> uniquePostCommentAuthorByUserPosts = postService
        .getUniqueCommentAuthorsByPostId(userPostsByAuthorId);
    Set<Long> editedPostIds = postVersionService.getDistinctEditedUserPostIds();
    model.addAttribute("currentLoggedUserFriendsIds", currentLoggedUserFriendsIds);
    model.addAttribute("posts", userPostsByAuthorId);
    model.addAttribute("user", userDisplayDtoOptional.get());
    model.addAttribute("currentLoggedUser", currentLoggedUser);
    model.addAttribute("uniqueCommentAuthorsPerPost", uniquePostCommentAuthorByUserPosts);
    model.addAttribute("editedPostIds", editedPostIds);
    return "user-activity";
  }

  /**
   * Displays user's friends list with optional name filtering.
   *
   * @param firstName user's first name
   * @param lastName user's last name
   * @param id user's ID
   * @param query optional search term for filtering friends
   * @param model model to pass data to the view
   * @return name of the view to render
   * @throws UserNotFoundException if user is not found
   */
  @GetMapping("/{firstName}.{lastName}.{id}/friends")
  public String displayAllFriends(@PathVariable String firstName,
                                     @PathVariable String lastName,
                                     @PathVariable Long id, Authentication authentication,
                                     @RequestParam(name = "query", required = false) String query,
                                     Model model) {
    UserDisplayDto displayedUser = userService.findUserByNameAndId(firstName, lastName, id)
        .orElseThrow(() -> new UserNotFoundException("Brak takiego użytkownika"));
    List<UserDisplayDto> allFriends = userService.getUserFriendsListByUserId(id);
    if (query != null) {
      allFriends = userService.filterUserFriendsByQuery(allFriends, query);
    }
    model.addAttribute("user", displayedUser);
    model.addAttribute("allFriends", allFriends);
    model.addAttribute("tab", "all");
    return "user-friends";
  }

  /**
   * Displays mutual friends between the logged-in user and the specified user.
   *
   * @param firstName user's first name
   * @param lastName user's last name
   * @param id user's ID
   * @param authentication current authentication object
   * @param query optional search term for filtering mutual friends
   * @param model model to pass data to the view
   * @return name of the view to render
   */
  @GetMapping("/{firstName}.{lastName}.{id}/friends_mutual")
  public String displayMutualFriends(@PathVariable String firstName,
                                  @PathVariable String lastName,
                                  @PathVariable Long id,
                                  Authentication authentication,
                                  @RequestParam(name = "query", required = false) String query,
                                  Model model) {
    UserDisplayDto displayedUser = userService.findUserByNameAndId(firstName, lastName, id)
        .orElseThrow(() -> new UserNotFoundException("Brak takiego użytkownika"));
    List<UserDisplayDto> mutualFriendsByUserId = userService.getMutualFriendsByUserId(id,
        authentication);
    if (query != null) {
      mutualFriendsByUserId = userService.filterUserFriendsByQuery(mutualFriendsByUserId, query);
    }
    model.addAttribute("user", displayedUser);
    model.addAttribute("mutualFriends", mutualFriendsByUserId);
    model.addAttribute("tab", "mutual");
    return "user-friends";
  }

}