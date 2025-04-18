package pl.mateusz.example.friendoo.comment.user;

import org.springframework.stereotype.Service;
import pl.mateusz.example.friendoo.exceptions.UserNotFoundException;
import pl.mateusz.example.friendoo.post.user.UserPost;
import pl.mateusz.example.friendoo.post.user.UserPostRepository;
import pl.mateusz.example.friendoo.user.User;
import pl.mateusz.example.friendoo.user.UserRepository;

/**
 * Service class for managing UserPostComment entities.
 * Provides business logic and operations related to comments under user posts.
 */
@Service
public class UserPostCommentService {
  private final UserPostCommentRepository userPostCommentRepository;
  private final UserRepository userRepository;
  private final UserPostRepository userPostRepository;

  /**
   * Constructor for UserPostCommentService.
   *
   * @param userPostCommentRepository the repository for user post comments
   * @param userRepository            the repository for users
   * @param userPostRepository        the repository for user posts
   */
  public UserPostCommentService(UserPostCommentRepository userPostCommentRepository,
                                UserRepository userRepository,
                                UserPostRepository userPostRepository) {
    this.userPostCommentRepository = userPostCommentRepository;
    this.userRepository = userRepository;
    this.userPostRepository = userPostRepository;
  }

}
