package pl.mateusz.example.friendoo.post.user;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pl.mateusz.example.friendoo.post.PostDto;
import pl.mateusz.example.friendoo.post.PostService;

/**
 REST controller for handling user post-related operations.
 Provides endpoints under the /api base path. */
@RestController
@RequestMapping("/api")
public class UserPostController {

  private final PostService postService;

  public UserPostController(PostService postService) {
    this.postService = postService;
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

}
