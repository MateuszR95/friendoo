package pl.mateusz.example.friendoo.post.user;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import pl.mateusz.example.friendoo.reaction.user.UserPostReactionDto;
import pl.mateusz.example.friendoo.reaction.user.UserPostReactionService;

/**
 * Service class for managing user posts.
 * It provides methods to save and retrieve user posts.
 */
@Service
public class UserPostService {

  private final UserPostRepository userPostRepository;

  private final UserPostReactionService userPostReactionService;

  /**
   * Constructor for UserPostService.
   *
   * @param userPostRepository         the user post repository
   */
  public UserPostService(UserPostRepository userPostRepository,
                         UserPostReactionService userPostReactionService) {
    this.userPostRepository = userPostRepository;
    this.userPostReactionService = userPostReactionService;
  }

  /**
   * Retrieves a set of user posts by the author's ID.
   *
   * @param authorId the ID of the author whose posts are to be retrieved
   * @return a list of UserPostDto representing the user's posts
   */
  public List<UserPostDto> getUserPostsByAuthorId(Long authorId) {
    return userPostRepository.findUserPostsByAuthorIdOrderByCreatedAtDesc(authorId)
      .stream()
      .map(this::createUserPostFromEntity)
      .collect(Collectors.toList());
  }

  /**
   * Converts a {@link UserPost} entity to a {@link UserPostDto}, including its reactions.
   *
   * @param userPost the post entity to convert
   * @return the corresponding DTO with post data and reactions
   */
  private UserPostDto createUserPostFromEntity(UserPost userPost) {
    List<UserPostReactionDto> reactionsByPostId = userPostReactionService
        .getReactionsByPostId(userPost.getId());
    return UserPostDtoMapper.mapToUserPostDto(userPost, reactionsByPostId);
  }

}
