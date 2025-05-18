package pl.mateusz.example.friendoo.reaction;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling reactions to post comments.
 */
@RestController
@RequestMapping("/api")
public class PostCommentReactionController {

  private final PostCommentReactionService postCommentReactionService;

  public PostCommentReactionController(PostCommentReactionService postCommentReactionService) {
    this.postCommentReactionService = postCommentReactionService;
  }

  /**
   * Endpoint to react to a post comment.
   *
   * @param dto the PostCommentReactionDto containing reaction details
   * @param authentication the authentication object containing user details
   * @return ResponseEntity containing the PostCommentReactionDto
   */
  @PostMapping("/post-comments/reactions")
  public ResponseEntity<PostCommentReactionDto> reactOnPostComment(@RequestBody
               PostCommentReactionDto dto, Authentication authentication) {
    PostCommentReactionDto postCommentReactionDto = postCommentReactionService
        .reactToPostComment(dto, authentication);
    if (postCommentReactionDto == null) {
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.ok(postCommentReactionDto);
    }

  }
}
