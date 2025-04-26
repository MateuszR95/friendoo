package pl.mateusz.example.friendoo.reaction;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling user post reactions.
 * Provides endpoints for reacting to user posts.
 */
@RestController
@RequestMapping("/api")
public class PostReactionController {

  private final PostReactionService postReactionService;

  public PostReactionController(PostReactionService postReactionService) {
    this.postReactionService = postReactionService;
  }

  /**
   * Endpoint to react to a user post.
   *
   * @param dto the PostReactionDto containing reaction details
   * @param authentication the authentication object containing user details
   * @return ResponseEntity containing the PostReactionDto
   */
  @PostMapping("/reactions")
  @ResponseBody
  public ResponseEntity<PostReactionDto> reactOnUserPost(@RequestBody PostReactionDto dto,
                                                             Authentication authentication) {
    PostReactionDto postReactionDto = postReactionService.reactToUserPost(dto, authentication);
    if (postReactionDto == null) {
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.ok(postReactionDto);
    }
  }

}
