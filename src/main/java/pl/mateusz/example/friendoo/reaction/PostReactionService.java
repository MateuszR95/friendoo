package pl.mateusz.example.friendoo.reaction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.mateusz.example.friendoo.exceptions.ReactionNotFoundException;
import pl.mateusz.example.friendoo.exceptions.UserNotFoundException;
import pl.mateusz.example.friendoo.exceptions.UserPostNotFoundException;
import pl.mateusz.example.friendoo.post.user.UserPost;
import pl.mateusz.example.friendoo.post.user.UserPostRepository;
import pl.mateusz.example.friendoo.user.User;
import pl.mateusz.example.friendoo.user.UserRepository;

/**
 * Service class for managing user post reactions.
 * This class provides methods to handle reactions to user posts.
 */
@Service
public class PostReactionService {

  private final PostReactionRepository postReactionRepository;
  private final UserRepository userRepository;
  private final UserPostRepository userPostRepository;
  private final ReactionRepository reactionRepository;

  /**
   * Constructor for PostReactionService.
   *
   * @param postReactionRepository the repository for user post reactions
   * @param userRepository         the repository for users
   * @param userPostRepository     the repository for user posts
   * @param reactionRepository     the repository for reactions
   */
  public PostReactionService(PostReactionRepository postReactionRepository,
                             UserRepository userRepository, UserPostRepository userPostRepository,
                             ReactionRepository reactionRepository) {
    this.postReactionRepository = postReactionRepository;
    this.userRepository = userRepository;
    this.userPostRepository = userPostRepository;
    this.reactionRepository = reactionRepository;
  }

  /**
   * Retrieves reactions for multiple user posts in a single query.
   * This method helps avoid the N+1 query issue by fetching all reactions
   * for the given list of post IDs in bulk, mapping them to DTOs, and grouping
   * them by the corresponding post ID.
   *
   * @param postIds a list of user post IDs for which reactions should be retrieved
   * @return a map where the key is the user post ID and the value is a list of PostReactionDto
   */
  public Map<Long, List<PostReactionDto>> getReactionsForMultipleUserPosts(List<Long> postIds) {
    return postReactionRepository.findByUserPostIds(postIds)
      .stream()
      .map(PostReactionDtoMapper::mapToDto)
      .collect(Collectors.groupingBy(PostReactionDto::getUserPostId));
  }


  /**
   * Adds, updates, or removes a user's reaction to a specific user post.
   * <p>
   * If the reaction already exists and is of the same type, it is removed (toggled off).
   * If the reaction exists but is of a different type, the existing reaction is updated.
   * If no previous reaction exists, a new reaction is created and saved.
   * </p>
   *
   * @param postReactionDto Data transfer object containing details about the
   *                       reaction and target post.
   * @param authentication The authentication token containing user details.
   * @throws UserNotFoundException if the authenticated user cannot be found in the database.
   * @throws UserPostNotFoundException if the post specified by {@code postReactionDto}
   *                                   cannot be found.
   * @throws ReactionNotFoundException if the reaction type specified by {@code postReactionDto}
   *                                   cannot be found.
   */
  @Transactional
  public void reactToUserPost(PostReactionDto postReactionDto,
                              Authentication authentication) {
    String userEmail = ((UserDetails) authentication.getPrincipal()).getUsername();
    User user = userRepository.findUserByEmail(userEmail).orElseThrow(
        () -> new UserNotFoundException("Nie znaleziono użytkownika"));
    UserPost userPost = userPostRepository.findById(postReactionDto.getUserPostId())
        .orElseThrow(() -> new UserPostNotFoundException("Nie znaleziono posta"));
    Reaction reaction = reactionRepository.findByReactionType(postReactionDto.getReactionType())
        .orElseThrow(() -> new ReactionNotFoundException("Nie znaleziono reakcji"));
    Optional<PostReaction> reactionOnPostOptional = postReactionRepository
        .findByUserPostIdAndAuthorId(userPost.getId(), user.getId());
    if (reactionOnPostOptional.isPresent()) {
      PostReaction postReaction = reactionOnPostOptional.get();
      if (postReaction.getReaction().getReactionType().equals(postReactionDto
          .getReactionType())) {
        postReactionRepository.delete(postReaction);
      } else {
        postReaction.setReaction(reaction);
        postReaction.setReactionTime(LocalDateTime.now());
        postReactionRepository.save(postReaction);
      }
    } else {
      PostReaction postReaction = createPostReactionFromDto(postReactionDto,
          authentication);
      postReactionRepository.save(postReaction);
    }
  }

  private PostReaction createPostReactionFromDto(PostReactionDto postReactionDto,
                                                 Authentication authentication) {
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    PostReaction postReaction = new PostReaction();
    User user = userRepository.findUserByEmail(userDetails.getUsername()).orElseThrow(
        () -> new UserNotFoundException("Nie znaleziono użytkownika"));
    postReaction.setAuthor(user);
    UserPost userPost = userPostRepository.findById(postReactionDto.getUserPostId())
        .orElseThrow(() -> new UserPostNotFoundException("Nie znaleziono posta"));
    postReaction.setUserPost(userPost);
    Reaction reaction = reactionRepository.findByReactionType(postReactionDto.getReactionType())
        .orElseThrow(() -> new ReactionNotFoundException("Nie znaleziono reakcji"));
    postReaction.setReaction(reaction);
    postReaction.setReactionTime(LocalDateTime.now());
    return postReaction;
  }

}
