package pl.mateusz.example.friendoo.post;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.mateusz.example.friendoo.comment.user.PostComment;
import pl.mateusz.example.friendoo.comment.user.PostCommentRepository;
import pl.mateusz.example.friendoo.exceptions.PageNotFoundException;
import pl.mateusz.example.friendoo.exceptions.UserNotFoundException;
import pl.mateusz.example.friendoo.exceptions.UserPostNotFoundException;
import pl.mateusz.example.friendoo.page.Page;
import pl.mateusz.example.friendoo.page.PageRepository;
import pl.mateusz.example.friendoo.photo.Photo;
import pl.mateusz.example.friendoo.photo.PhotoRepository;
import pl.mateusz.example.friendoo.post.page.PagePost;
import pl.mateusz.example.friendoo.post.user.UserPost;
import pl.mateusz.example.friendoo.post.user.UserPostRepository;
import pl.mateusz.example.friendoo.reaction.PostReaction;
import pl.mateusz.example.friendoo.reaction.PostReactionDto;
import pl.mateusz.example.friendoo.reaction.PostReactionRepository;
import pl.mateusz.example.friendoo.reaction.PostReactionService;
import pl.mateusz.example.friendoo.user.User;
import pl.mateusz.example.friendoo.user.UserRepository;

/**
 * Service for handling user and page posts, including retrieving posts
 * and related reactions.
 */
@Service
public class PostService {

  private final UserPostRepository userPostRepository;
  private final PostReactionService postReactionService;

  private final UserRepository userRepository;

  private final PostCommentRepository postCommentRepository;

  private final PostReactionRepository postReactionRepository;

  private final PhotoRepository photoRepository;

  private final PageRepository pageRepository;

  /**
   * Constructor for PostService.
   *
   * @param userPostRepository the repository for user posts
   * @param postReactionService the service for handling post reactions
   * @param userRepository the repository for users
   * @param postCommentRepository the repository for post comments
   * @param postReactionRepository the repository for post reactions
   * @param photoRepository the repository for photos
   * @param pageRepository the repository for pages
   */
  public PostService(UserPostRepository userPostRepository, PostReactionService postReactionService,
                     UserRepository userRepository, PostCommentRepository postCommentRepository,
                     PostReactionRepository postReactionRepository, PhotoRepository photoRepository,
                     PageRepository pageRepository) {
    this.userPostRepository = userPostRepository;
    this.postReactionService = postReactionService;
    this.userRepository = userRepository;
    this.postCommentRepository = postCommentRepository;
    this.postReactionRepository = postReactionRepository;
    this.photoRepository = photoRepository;
    this.pageRepository = pageRepository;
  }

  /**
   * Retrieves a list of user posts by the author ID.
   *
   * @param authorId the ID of the author
   * @return a list of PostDto objects representing the user's posts
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

  /**
   * Adds a new user post.
   *
   * @param postDto        post details
   * @param authentication user authentication
   * @return saved post as {@link PostDto}
   * @throws UserNotFoundException if user not found
   */
  @Transactional
  public PostDto addUserPost(PostDto postDto, Authentication authentication) {
    String userEmail = ((UserDetails) authentication.getPrincipal()).getUsername();
    User user = userRepository.findUserByEmail(userEmail)
        .orElseThrow(() -> new UserNotFoundException("Nie znaleziono użytkownika"));
    UserPost userPost = new UserPost();
    userPost.setAuthor(user);
    userPost.setContent(postDto.getContent());
    userPost.setCreatedAt(LocalDateTime.now());
    UserPost saved = userPostRepository.save(userPost);
    MappablePost wrapper = new UserPostWrapper(saved);
    return wrapper.toDto(List.of());
  }

  private UserPost createUserPostEntity(PostDto postDto) {
    User user = userRepository.findById(postDto.getUserAuthorId())
        .orElseThrow(() -> new UserNotFoundException("Nie znaleziono użytkownika"));

    Set<PostComment> comments = postCommentRepository.findAllByUserPostId(postDto.getId());
    Set<PostReaction> reactions = postReactionRepository.findAllByUserPostId(postDto.getId());
    List<Photo> photos = photoRepository.findAllByUserPostId(postDto.getId());

    return PostMapper.mapFromDtoToUserPost(postDto, user, comments, reactions, photos);
  }

  private PagePost createPagePostEntity(PostDto postDto) {
    User user = userRepository.findById(postDto.getUserAuthorId())
        .orElseThrow(() -> new UserNotFoundException("Nie znaleziono użytkownika"));
    Page page = pageRepository.findById(postDto.getPageAuthorId())
        .orElseThrow(() -> new PageNotFoundException("Nie znaleziono strony"));
    Set<PostComment> comments = postCommentRepository.findAllByPagePostId(postDto.getId());
    Set<PostReaction> reactions = postReactionRepository.findAllByPagePostId(postDto.getId());
    List<Photo> photos = photoRepository.findAllByPagePostId(postDto.getId());
    return PostMapper.mapFromDtoToPagePost(postDto, page, user, comments, reactions, photos);
  }


}
