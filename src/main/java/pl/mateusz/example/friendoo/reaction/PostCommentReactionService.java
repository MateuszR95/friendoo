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
import pl.mateusz.example.friendoo.comment.PostComment;
import pl.mateusz.example.friendoo.comment.PostCommentRepository;
import pl.mateusz.example.friendoo.exceptions.PostCommentNotFoundException;
import pl.mateusz.example.friendoo.exceptions.ReactionNotFoundException;
import pl.mateusz.example.friendoo.exceptions.UserNotFoundException;
import pl.mateusz.example.friendoo.user.User;
import pl.mateusz.example.friendoo.user.UserRepository;
import pl.mateusz.example.friendoo.user.UserService;

/**
 * Service for handling reactions to post comments.
 */
@Service
public class PostCommentReactionService {

  private final PostCommentReactionRepository postCommentReactionRepository;
  private final UserService userService;

  private final PostCommentRepository postCommentRepository;
  private final ReactionRepository reactionRepository;

  private final UserRepository userRepository;

  /**
   * Constructor for PostCommentReactionService.
   *
   * @param postCommentReactionRepository the repository for post comment reactions
   * @param userService the service for user-related operations
   * @param postCommentRepository the repository for post comments
   * @param reactionRepository the repository for reactions
   * @param userRepository the repository for users
   */
  public PostCommentReactionService(PostCommentReactionRepository postCommentReactionRepository,
                                    UserService userService,
                                    PostCommentRepository postCommentRepository,
                                    ReactionRepository reactionRepository,
                                    UserRepository userRepository) {
    this.postCommentReactionRepository = postCommentReactionRepository;
    this.userService = userService;
    this.postCommentRepository = postCommentRepository;
    this.reactionRepository = reactionRepository;
    this.userRepository = userRepository;
  }

  /**
   * Reacts to a post comment.
   *
   * @param postCommentReactionDto the DTO containing reaction details
   * @param authentication the authentication object containing user details
   * @return the updated PostCommentReactionDto
   */
  @Transactional
  public PostCommentReactionDto reactToPostComment(PostCommentReactionDto postCommentReactionDto,
                                                   Authentication authentication) {
    User user = userService.getUserFromAuthentication(authentication);
    PostComment postComment = postCommentRepository.findById(postCommentReactionDto
        .getPostCommentId()).orElseThrow(() ->
        new PostCommentNotFoundException("Nie znaleziono komentarza do postu"));
    Reaction reaction = findReactionByType(postCommentReactionDto);
    Optional<PostCommentReaction> postCommentReactionOptional = postCommentReactionRepository
        .findByCommentIdAndAndAuthorId(postComment.getId(), user.getId());
    if (postCommentReactionOptional.isPresent()) {
      PostCommentReaction postCommentReaction = postCommentReactionOptional.get();
      return handleExistingReaction(postCommentReactionDto, postCommentReaction, reaction);
    } else {
      return createAndSaveNewReaction(postCommentReactionDto, authentication);

    }
  }

  private PostCommentReaction createPostCommentReactionFromDto(PostCommentReactionDto
                                       postReactionDto, Authentication authentication) {
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    PostCommentReaction postCommentReaction = new PostCommentReaction();
    User user = userRepository.findUserByEmail(userDetails.getUsername()).orElseThrow(
        () -> new UserNotFoundException("Nie znaleziono użytkownika"));
    postCommentReaction.setAuthor(user);
    PostComment postComment = postCommentRepository.findById(postReactionDto.getPostCommentId())
        .orElseThrow(() -> new PostCommentNotFoundException("Nie znaleziono komentarza"));
    postCommentReaction.setComment(postComment);
    Reaction reaction = findReactionByType(postReactionDto);
    postCommentReaction.setReaction(reaction);
    postCommentReaction.setReactionTime(LocalDateTime.now());
    return postCommentReaction;
  }

  private PostCommentReactionDto createAndSaveNewReaction(PostCommentReactionDto
                                        postCommentReactionDto, Authentication authentication) {
    PostCommentReaction postCommentReaction = createPostCommentReactionFromDto(
        postCommentReactionDto, authentication);
    PostCommentReaction savedPostCommentReaction = postCommentReactionRepository
        .save(postCommentReaction);
    return PostCommentReactionDtoMapper.mapToPostCommentReactionDto(savedPostCommentReaction);
  }

  private PostCommentReactionDto handleExistingReaction(PostCommentReactionDto
              postCommentReactionDto, PostCommentReaction postCommentReaction, Reaction reaction) {
    if (postCommentReaction.getReaction().getReactionType()
        .equals(postCommentReactionDto.getReactionType())) {
      postCommentReactionRepository.delete(postCommentReaction);
      return null;
    } else {
      postCommentReaction.setReaction(reaction);
      postCommentReaction.setReactionTime(LocalDateTime.now());
      PostCommentReaction updatedPostCommentReaction = postCommentReactionRepository
          .save(postCommentReaction);
      return PostCommentReactionDtoMapper.mapToPostCommentReactionDto(updatedPostCommentReaction);
    }
  }

  private Reaction findReactionByType(PostCommentReactionDto reactionDto) {
    return reactionRepository.findByReactionType(reactionDto.getReactionType())
      .orElseThrow(() -> new ReactionNotFoundException("Nie znaleziono reakcji"));
  }


  /**
   * Retrieves reactions for multiple post comments in a single query.
   * This method helps avoid the N+1 query issue by fetching all reactions
   * for the given list of post comment IDs in bulk, mapping them to DTOs,
   * and grouping them by post comment ID.
   *
   * @param postCommentIds the list of post comment IDs
   * @return a map of post comment IDs to sets of PostCommentReactionDto
   */
  public Map<Long, List<PostCommentReactionDto>> getReactionsForMultipleUserPostComments(
        List<Long> postCommentIds) {
    return postCommentReactionRepository.findByCommentIdIn(postCommentIds).stream()
      .map(PostCommentReactionDtoMapper::mapToPostCommentReactionDto)
      .collect(Collectors.groupingBy(
        PostCommentReactionDto::getPostCommentId,
        Collectors.toList()
      ));
  }


}



