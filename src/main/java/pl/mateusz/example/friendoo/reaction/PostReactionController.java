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

  @PostMapping("/reactions")
  @ResponseBody
  public ResponseEntity<PostReactionDto> reactOnUserPost(@RequestBody PostReactionDto dto,
                                                             Authentication authentication) {
    postReactionService.reactToUserPost(dto, authentication);
    return ResponseEntity.ok(dto);
  }

}
