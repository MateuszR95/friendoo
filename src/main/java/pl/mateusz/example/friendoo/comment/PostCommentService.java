package pl.mateusz.example.friendoo.comment;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.mateusz.example.friendoo.exceptions.AccessDeniedException;
import pl.mateusz.example.friendoo.exceptions.PostCommentNotFoundException;
import pl.mateusz.example.friendoo.exceptions.UserNotFoundException;
import pl.mateusz.example.friendoo.post.user.UserPost;
import pl.mateusz.example.friendoo.post.user.UserPostRepository;
import pl.mateusz.example.friendoo.user.User;
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
   * @param postCommentRepository the repository for post comments
   * @param userRepository the repository for users
   * @param userPostRepository the repository for user posts
   */
  public PostCommentService(PostCommentRepository postCommentRepository,
                            UserRepository userRepository, UserPostRepository userPostRepository) {
    this.postCommentRepository = postCommentRepository;
    this.userRepository = userRepository;
    this.userPostRepository = userPostRepository;
  }

  /**
   * Adds a new comment to a user post.
   *
   * @param postCommentDto the DTO containing the comment details
   * @param authentication the authentication object containing user details
   * @return the saved PostCommentDto
   */
  @Transactional
  public PostCommentDto addUserPostComment(PostCommentDto postCommentDto,
                                           Authentication authentication) {
    User user = getAuthenticatedUser(authentication);
    PostComment postComment = new PostComment();
    UserPost userPost = userPostRepository.findById(postCommentDto.getUserPostId())
        .orElseThrow(() -> new UserNotFoundException("Nie znaleziono postu użytkownika"));
    if (postCommentDto.getQuotedCommentId() != null) {
      PostComment quotedComment = postCommentRepository.findById(postCommentDto
          .getQuotedCommentId())
          .orElseThrow(() -> new PostCommentNotFoundException(
            "Nie znaleziono komentarza do postu"));
      postComment.setQuotedComment(quotedComment);
    }
    postComment.setUserPost(userPost);
    postComment.setAuthor(user);
    postComment.setContent(postCommentDto.getContent());
    postComment.setCreatedAt(LocalDateTime.now());
    PostComment savedUserPostComment = postCommentRepository.save(postComment);
    return PostCommentDtoMapper.mapToDto(savedUserPostComment);
  }

  /**
   * Retrieves comments for multiple user posts in a single query.
   * This method helps avoid the N+1 query issue by fetching all comments
   * for the given list of post IDs in bulk, mapping them to DTOs, and grouping
   * them by post ID.
   *
   * @param postIds the list of user post IDs
   * @return a map where the key is the user post ID and the value is a list of PostCommentDto
   */
  public Map<Long, List<PostCommentDto>> getCommentsForMultipleUserPosts(List<Long> postIds) {
    return postCommentRepository.findByUserPostIds(postIds)
      .stream()
      .map(PostCommentDtoMapper::mapToDto)
      .collect(Collectors.groupingBy(PostCommentDto::getUserPostId));
  }

  /**
   * Retrieves sorted user post comments by post ID.
   *
   * @param postId the ID of the user post
   * @param sortOrder the order in which to sort the comments (newest, oldest, mostPopular)
   * @return a list of sorted PostCommentDto
   */
  public List<PostCommentDto> getSortedUserPostCommentsByPostId(Long postId, String sortOrder) {
    List<PostCommentDto> userPostComments = postCommentRepository.findAllByUserPostId(postId)
        .stream()
        .map(PostCommentDtoMapper::mapToDto)
        .toList();
    Comparator<PostCommentDto> comparator = switch (sortOrder) {
      case "oldest" -> Comparator.comparing(PostCommentDto::getCreatedAt);
      case "mostPopular" -> Comparator.comparing(PostCommentDto::getReactionsCount).reversed();
      default -> Comparator.comparing(PostCommentDto::getCreatedAt).reversed();
    };
    return userPostComments.stream()
        .sorted(comparator)
        .collect(Collectors.toList());
  }

  /**
   * Deletes a user post comment by its ID.
   *
   * @param postCommentId the ID of the post comment to delete
   * @param authentication the authentication object containing user details
   */
  @Transactional
  public void deleteUserPostComment(Long postCommentId, Authentication authentication) {
    User loggedUser = getAuthenticatedUser(authentication);
    PostComment postComment = postCommentRepository.findById(postCommentId)
        .orElseThrow(() -> new PostCommentNotFoundException("Nie znaleziono posta"));
    if (!loggedUser.getId().equals(postComment.getAuthor().getId())) {
      throw new AccessDeniedException("Brak autoryzacji do usunięcia komentarza");
    }
    postCommentRepository.deleteById(postCommentId);
  }

  /**
   * Edits a user post comment by its ID.
   *
   * @param postCommentId the ID of the post comment to edit
   * @param dto the DTO containing the updated comment details
   * @param authentication the authentication object containing user details
   * @return the edited PostCommentDto
   */
  @Transactional
  public PostCommentDto editUserPostComment(Long postCommentId, PostCommentEditDto dto,
                                  Authentication authentication) {
    User loggedUser = getAuthenticatedUser(authentication);
    PostComment postComment = postCommentRepository.findById(postCommentId)
        .orElseThrow(() -> new PostCommentNotFoundException("Nie znaleziono komentarza do posta"));
    if (!postComment.getAuthor().getId().equals(loggedUser.getId())) {
      throw new AccessDeniedException("Brak autoryzacji do edytowania komentarza");
    }
    postComment.setContent(dto.getContent());
    postComment.setEditedAt(LocalDateTime.now());
    PostComment editedPostComment = postCommentRepository.save(postComment);
    return PostCommentDtoMapper.mapToDto(editedPostComment);
  }


  private User getAuthenticatedUser(Authentication authentication) {
    String currentLoggedUserEmail = ((UserDetails) authentication.getPrincipal()).getUsername();
    return userRepository.findUserByEmail(currentLoggedUserEmail)
        .orElseThrow(() -> new UserNotFoundException("Nie znaleziono użytkownika"));
  }
}
