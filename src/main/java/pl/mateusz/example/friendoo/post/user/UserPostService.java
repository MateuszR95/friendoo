package pl.mateusz.example.friendoo.post.user;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import pl.mateusz.example.friendoo.post.MappablePost;
import pl.mateusz.example.friendoo.post.PostDto;
import pl.mateusz.example.friendoo.post.UserPostWrapper;
import pl.mateusz.example.friendoo.reaction.PostReactionDto;
import pl.mateusz.example.friendoo.reaction.PostReactionService;

/**
 * Service class for managing user posts.
 * It provides methods to save and retrieve user posts.
 */
@Service
public class UserPostService {

  private final UserPostRepository userPostRepository;

  private final PostReactionService postReactionService;

  public UserPostService(UserPostRepository userPostRepository,
                         PostReactionService postReactionService) {
    this.userPostRepository = userPostRepository;
    this.postReactionService = postReactionService;
  }

  /**
   * Retrieves a list of user posts authored by a specific user,
   * along with their associated reactions.
   * This method avoids the N+1 query problem by fetching all reactions
   * for the user's posts in a single batch query.
   *
   * @param authorId the ID of the user whose posts are being retrieved
   * @return a list of UserPostDto containing post data and reactions
   */
  public List<PostDto> getUserPostsByAuthorId(Long authorId) {
    List<UserPost> posts = userPostRepository.findUserPostsByAuthorIdOrderByCreatedAtDesc(authorId);
    List<Long> postIds = posts.stream().map(UserPost::getId).toList();
    Map<Long, List<PostReactionDto>> reactionsMap = postReactionService
        .getReactionsForMultipleUserPosts(postIds);
    return posts.stream()
      .map(post -> {
        MappablePost wrapper = new UserPostWrapper(post);
        return wrapper.toDto(reactionsMap.getOrDefault(post.getId(), List.of()));
      })
      .collect(Collectors.toList());
  }

}
