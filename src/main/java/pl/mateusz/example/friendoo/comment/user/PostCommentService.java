package pl.mateusz.example.friendoo.comment.user;

import org.springframework.stereotype.Service;
import pl.mateusz.example.friendoo.post.user.UserPostRepository;
import pl.mateusz.example.friendoo.user.UserRepository;


/**
 * Service class for managing user post comments.
 * Provides methods to interact with the PostCommentRepository and perform operations related
 * to user post comments.
 */
@Service
public class PostCommentService {
  private final PostCommentRepository postCommentRepository;
  private final UserRepository userRepository;
  private final UserPostRepository userPostRepository;

  /**
   * Constructor for PostCommentService.
   *
   * @param postCommentRepository the repository for managing post comments
   * @param userRepository        the repository for managing users
   * @param userPostRepository    the repository for managing user posts
   */
  public PostCommentService(PostCommentRepository postCommentRepository,
                            UserRepository userRepository,
                            UserPostRepository userPostRepository) {
    this.postCommentRepository = postCommentRepository;
    this.userRepository = userRepository;
    this.userPostRepository = userPostRepository;
  }
}
