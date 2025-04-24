package pl.mateusz.example.friendoo.comment.user;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 REST controller for handling user post-related operations.
 Provides endpoints under the /api base path. */
@RestController
@RequestMapping("/api")
public class UserPostController {

  private final PostCommentService postCommentService;

  public UserPostController(PostCommentService postCommentService) {
    this.postCommentService = postCommentService;
  }
}
