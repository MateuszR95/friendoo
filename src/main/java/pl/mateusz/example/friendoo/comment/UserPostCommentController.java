package pl.mateusz.example.friendoo.comment;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling user post comments.
 * Provides endpoints to add comments and retrieve comments for a specific post.
 */
@RestController
@RequestMapping("/api")
public class UserPostCommentController {

  private final PostCommentService postCommentService;

  public UserPostCommentController(PostCommentService postCommentService) {
    this.postCommentService = postCommentService;
  }

  /**
   * Adds a new comment to a user post.
   *
   * @param postCommentDto the DTO containing the comment details
   * @param authentication the authentication object containing user details
   * @return the saved PostCommentDto
   */
  @PostMapping("/user-post-comments")
  @ResponseBody
  public ResponseEntity<PostCommentDto> addCommentToUserPost(@RequestBody @Valid
                           PostCommentDto postCommentDto, Authentication authentication) {
    PostCommentDto savedUserPostComment = postCommentService
        .addUserPostComment(postCommentDto, authentication);
    return ResponseEntity.ok(savedUserPostComment);
  }

  /**
   * Retrieves comments for a specific user post.
   *
   * @param postId the ID of the user post
   * @param sortOrder the order in which to sort the comments (newest or oldest)
   * @return a list of PostCommentDto objects representing the comments for the specified post
   */
  @GetMapping("/user-post-comments/{postId}")
  public ResponseEntity<List<PostCommentDto>> getCommentsForPost(@PathVariable Long postId,
                                @RequestParam(defaultValue = "newest") String sortOrder) {
    List<PostCommentDto> userPostComments = postCommentService.getSortedUserPostCommentsByPostId(
        postId, sortOrder);
    return ResponseEntity.ok(userPostComments);
  }

  @DeleteMapping("/user-post-comments/{postCommentId}")
  public ResponseEntity<Void> deleteUserPostComment(@PathVariable Long postCommentId,
                                                    Authentication authentication) {
    postCommentService.deleteUserPostComment(postCommentId, authentication);
    return ResponseEntity.noContent().build();
  }

  /**
   * Edits a user post comment.
   *
   * @param postCommentId the ID of the comment to be edited
   * @param dto the DTO containing the updated comment details
   * @param authentication the authentication object containing user details
   * @return the updated PostCommentDto
   */
  @PutMapping("/user-post-comments/{postCommentId}")
  public ResponseEntity<PostCommentDto> editUserPostComment(@PathVariable Long postCommentId,
                     @RequestBody @Valid PostCommentEditDto dto, Authentication authentication) {
    PostCommentDto postCommentDto = postCommentService.editUserPostComment(postCommentId,
        dto, authentication);
    return ResponseEntity.ok(postCommentDto);
  }


}
