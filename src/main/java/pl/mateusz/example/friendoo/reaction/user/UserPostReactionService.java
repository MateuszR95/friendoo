package pl.mateusz.example.friendoo.reaction.user;

import java.time.LocalDateTime;
import java.util.List;
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
import pl.mateusz.example.friendoo.reaction.Reaction;
import pl.mateusz.example.friendoo.reaction.ReactionRepository;
import pl.mateusz.example.friendoo.user.User;
import pl.mateusz.example.friendoo.user.UserRepository;

/**
 * Service class for managing user post reactions.
 * This class provides methods to handle reactions to user posts.
 */
@Service
public class UserPostReactionService {

  private final UserPostReactionRepository userPostReactionRepository;
  private final UserRepository userRepository;
  private final UserPostRepository userPostRepository;
  private final ReactionRepository reactionRepository;

  /**
   * Constructor for UserPostReactionService.
   *
   * @param userPostReactionRepository the repository for user post reactions
   * @param userRepository             the repository for users
   * @param userPostRepository         the repository for user posts
   * @param reactionRepository         the repository for reactions
   */
  public UserPostReactionService(UserPostReactionRepository userPostReactionRepository,
                                 UserRepository userRepository,
                                 UserPostRepository userPostRepository,
                                 ReactionRepository reactionRepository) {
    this.userPostReactionRepository = userPostReactionRepository;
    this.userRepository = userRepository;
    this.userPostRepository = userPostRepository;
    this.reactionRepository = reactionRepository;
  }

  /**
   * Retrieves a list of reactions for a specific post by its ID.
   *
   * @param postId the ID of the post
   * @return a list of reactions for the specified post
   */
  public List<UserPostReactionDto> getReactionsByPostId(Long postId) {
    return userPostReactionRepository.findByPostIdOrderByReactionTimeDesc(postId)
      .stream()
      .map(UserPostReactionDtoMapper::mapToDto)
      .collect(Collectors.toList());
  }

  /**
   * Adds or updates a reaction to a user post.
   *
   * @param userPostReactionDto the DTO containing the reaction details
   * @param authentication      the authentication object containing user details
   */
  @Transactional
  public void reactToUserPost(UserPostReactionDto userPostReactionDto,
                              Authentication authentication) {
    String userEmail = ((UserDetails) authentication.getPrincipal()).getUsername();
    User user = userRepository.findUserByEmail(userEmail).orElseThrow(
        () -> new UserNotFoundException("Nie znaleziono użytkownika"));
    UserPost userPost = userPostRepository.findById(userPostReactionDto.getUserPostId())
        .orElseThrow(() -> new UserPostNotFoundException("Nie znaleziono posta"));
    Reaction reaction = reactionRepository.findByReactionType(userPostReactionDto.getReactionType())
        .orElseThrow(() -> new ReactionNotFoundException("Nie znaleziono reakcji"));
    Optional<UserPostReaction> reactionOnPostOptional = userPostReactionRepository
        .findByPostIdAndUserId(userPost.getId(), user.getId());
    if (reactionOnPostOptional.isPresent()) {
      UserPostReaction userPostReaction = reactionOnPostOptional.get();
      if (userPostReaction.getReaction().getReactionType().equals(userPostReactionDto
          .getReactionType())) {
        userPostReactionRepository.delete(userPostReaction);
      } else {
        userPostReaction.setReaction(reaction);
        userPostReaction.setReactionTime(LocalDateTime.now());
        userPostReactionRepository.save(userPostReaction);
      }
    } else {
      UserPostReaction userPostReaction = createUserPostReactionFromDto(userPostReactionDto,
          authentication);
      userPostReactionRepository.save(userPostReaction);
    }
  }

  private UserPostReaction createUserPostReactionFromDto(UserPostReactionDto userPostReactionDto,
                                                         Authentication authentication) {
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    UserPostReaction userPostReaction = new UserPostReaction();
    User user = userRepository.findUserByEmail(userDetails.getUsername()).orElseThrow(
        () -> new UserNotFoundException("Nie znaleziono użytkownika"));
    userPostReaction.setUser(user);
    UserPost userPost = userPostRepository.findById(userPostReactionDto.getUserPostId())
        .orElseThrow(() -> new UserPostNotFoundException("Nie znaleziono posta"));
    userPostReaction.setPost(userPost);
    Reaction reaction = reactionRepository.findByReactionType(userPostReactionDto.getReactionType())
        .orElseThrow(() -> new ReactionNotFoundException("Nie znaleziono reakcji"));
    userPostReaction.setReaction(reaction);
    userPostReaction.setReactionTime(LocalDateTime.now());
    return userPostReaction;
  }

}
