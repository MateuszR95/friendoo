package pl.mateusz.example.friendoo.post.user;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pl.mateusz.example.friendoo.post.PostDto;
import pl.mateusz.example.friendoo.post.PostEditDto;
import pl.mateusz.example.friendoo.post.PostService;
import pl.mateusz.example.friendoo.post.postversion.PostVersionDto;
import pl.mateusz.example.friendoo.post.postversion.PostVersionService;

import java.util.List;

/**
 REST controller for handling user post-related operations.
 Provides endpoints under the /api base path. */
@RestController
@RequestMapping("/api")
public class UserPostController {

  private final PostService postService;

  private final PostVersionService postVersionService;

  public UserPostController(PostService postService, PostVersionService postVersionService) {
    this.postService = postService;
    this.postVersionService = postVersionService;
  }

  /**
   * Endpoint to publish a user post.
   *
   * @param postDto the post data transfer object containing post details
   * @param authentication the authentication object containing user details
   * @return ResponseEntity containing the saved PostDto
   */
  @PostMapping("/posts")
  @ResponseBody
  public ResponseEntity<PostDto> publishUserPost(@RequestBody @Valid PostDto postDto,
                                                 Authentication authentication) {
    PostDto savedPost = postService.addUserPost(postDto, authentication);
    return ResponseEntity.ok(savedPost);
  }

  @DeleteMapping("/posts/{userPostId}")
  public ResponseEntity<Void> deleteUserPost(@PathVariable Long userPostId,
                                              Authentication authentication) {
    postService.deleteUserPost(userPostId, authentication);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/posts/{userPostId}")
  public ResponseEntity<PostDto> editUserPost(@PathVariable Long userPostId,
          @RequestBody @Valid PostEditDto dto, Authentication authentication) {
    PostDto postDto = postService.editUserPost(userPostId, dto, authentication);
    return ResponseEntity.ok(postDto);
  }

  /**
   * Endpoint to retrieve all versions of a user post.
   *
   * @param userPostId the ID of the user post
   * @return ResponseEntity containing a list of PostVersionDto
   */
  @GetMapping("posts/{userPostId}/versions")
  public ResponseEntity<List<PostVersionDto>> getPostVersions(@PathVariable Long userPostId) {
    List<PostVersionDto> postVersionsByUserPostId = postVersionService
        .getPostVersionsByUserPostId(userPostId);
    return ResponseEntity.ok(postVersionsByUserPostId);
  }
}
