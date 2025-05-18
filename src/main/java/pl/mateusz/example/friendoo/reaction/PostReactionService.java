package pl.mateusz.example.friendoo.reaction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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
import pl.mateusz.example.friendoo.user.UserService;

/**
 * Service class for managing user post reactions.
 * This class provides methods to handle reactions to user posts.
 */
@Service
public class PostReactionService {

  private final PostReactionRepository postReactionRepository;
  private final UserRepository userRepository;

  private final UserService userService;

  private final UserPostRepository userPostRepository;
  private final ReactionRepository reactionRepository;

  /**
   * Constructor for PostReactionService.
   *
   * @param postReactionRepository the repository for post reactions
   * @param userRepository the repository for users
   * @param userService the service for user-related operations
   * @param userPostRepository the repository for user posts
   * @param reactionRepository the repository for reactions
   */
  public PostReactionService(PostReactionRepository postReactionRepository,
                             UserRepository userRepository, UserService userService,
                             UserPostRepository userPostRepository,
                             ReactionRepository reactionRepository) {
    this.postReactionRepository = postReactionRepository;
    this.userRepository = userRepository;
    this.userService = userService;
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
  public Map<Long, Set<PostReactionDto>> getReactionsForMultipleUserPosts(List<Long> postIds) {
    return postReactionRepository.findByUserPostIds(postIds)
      .stream()
      .map(PostReactionDtoMapper::mapToDto)
      .collect(Collectors.groupingBy(
        PostReactionDto::getUserPostId,
        Collectors.toSet()
      ));
  }

  /**
   * Handles user post reactions.
   *
   * @param postReactionDto reaction details
   * @param authentication  user authentication
   * @return updated or new reaction, or null if removed
   * @throws UserNotFoundException       if user not found
   * @throws UserPostNotFoundException   if post not found
   * @throws ReactionNotFoundException   if reaction type not found
   */
  @Transactional
  public PostReactionDto reactToUserPost(PostReactionDto postReactionDto,
                              Authentication authentication) {
    User user = userService.getUserFromAuthentication(authentication);
    UserPost userPost = userPostRepository.findById(postReactionDto.getUserPostId()).orElseThrow(
        () -> new UserPostNotFoundException("Nie znaleziono posta"));
    Reaction reaction = findReactionByType(postReactionDto);
    Optional<PostReaction> reactionOnPostOptional = postReactionRepository
        .findByUserPostIdAndAuthorId(userPost.getId(), user.getId());
    if (reactionOnPostOptional.isPresent()) {
      PostReaction postReaction = reactionOnPostOptional.get();
      return handleExistingReaction(postReactionDto, postReaction, reaction);
    } else {
      return createAndSaveNewReaction(postReactionDto, authentication);
    }
  }

  private PostReactionDto createAndSaveNewReaction(PostReactionDto postReactionDto,
                                        Authentication authentication) {
    PostReaction postReaction = createPostReactionFromDto(postReactionDto,
        authentication);
    PostReaction savedReaction = postReactionRepository.save(postReaction);
    return PostReactionDtoMapper.mapToDto(savedReaction);
  }

  private PostReactionDto handleExistingReaction(PostReactionDto postReactionDto,
                                      PostReaction postReaction,
                                      Reaction reaction) {
    if (postReaction.getReaction().getReactionType().equals(postReactionDto
        .getReactionType())) {
      postReactionRepository.delete(postReaction);
      return null;
    } else {
      postReaction.setReaction(reaction);
      postReaction.setReactionTime(LocalDateTime.now());
      PostReaction updatedReaction = postReactionRepository.save(postReaction);
      return PostReactionDtoMapper.mapToDto(updatedReaction);
    }
  }

  private Reaction findReactionByType(PostReactionDto postReactionDto) {
    return reactionRepository.findByReactionType(postReactionDto.getReactionType())
        .orElseThrow(() -> new ReactionNotFoundException("Nie znaleziono reakcji"));
  }

  private PostReaction createPostReactionFromDto(PostReactionDto postReactionDto,
                                                 Authentication authentication) {
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    PostReaction postReaction = new PostReaction();
    User user = userRepository.findUserByEmail(userDetails.getUsername()).orElseThrow(
        () -> new UserNotFoundException("Nie znaleziono użytkownika"));
    postReaction.setAuthor(user);
    UserPost userPost = userPostRepository.findById(postReactionDto.getUserPostId()).orElseThrow(
        () -> new UserPostNotFoundException("Nie znaleziono posta"));
    postReaction.setUserPost(userPost);
    Reaction reaction = findReactionByType(postReactionDto);
    postReaction.setReaction(reaction);
    postReaction.setReactionTime(LocalDateTime.now());
    return postReaction;
  }

}
