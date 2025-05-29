package pl.mateusz.example.friendoo.visit;

import java.time.LocalDateTime;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.mateusz.example.friendoo.user.User;
import pl.mateusz.example.friendoo.user.UserService;

/**
 * Service for user profile visit.
 */
@Service
public class UserProfileVisitService {

  private final UserProfileVisitRepository userProfileVisitRepository;
  private final UserService userService;

  public UserProfileVisitService(UserProfileVisitRepository userProfileVisitRepository,
                                 UserService userService) {
    this.userProfileVisitRepository = userProfileVisitRepository;
    this.userService = userService;
  }

  /**
   * Saves user profile visit.
   *
   * @param viewedUserId      the viewed user id
   * @param authentication    the authentication
   */
  @Transactional
  public void saveUserProfileVisit(Long viewedUserId,
                                   Authentication authentication) {
    User loggedUser = userService.getUserFromAuthentication(authentication);
    User viewedUser = userService.findUserById(viewedUserId);
    if (!loggedUser.getId().equals(viewedUserId)) {
      UserProfileVisit userProfileVisit = new UserProfileVisit();
      userProfileVisit.setViewer(loggedUser);
      userProfileVisit.setUserProfile(viewedUser);
      userProfileVisit.setVisitedAt(LocalDateTime.now());
      userProfileVisitRepository.save(userProfileVisit);
    }
  }
}
