package pl.mateusz.example.friendoo.reaction.user;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pl.mateusz.example.friendoo.post.user.UserPostService;

/**
 * Controller for handling user post reactions.
 * Provides endpoints for reacting to user posts.
 */
@RestController
@RequestMapping("/api")
public class UserPostReactionController {

  private final UserPostReactionService userPostReactionService;

  public UserPostReactionController(UserPostReactionService userPostReactionService) {
    this.userPostReactionService = userPostReactionService;
  }

  @PostMapping("/reactions")
  @ResponseBody
  public ResponseEntity<UserPostReactionDto> reactOnUserPost(@RequestBody UserPostReactionDto dto,
                                                             Authentication authentication) {
    userPostReactionService.reactToUserPost(dto, authentication);
    return ResponseEntity.ok(dto);
  }

}
