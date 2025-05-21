package pl.mateusz.example.friendoo.post;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.mateusz.example.friendoo.comment.PostComment;
import pl.mateusz.example.friendoo.comment.PostCommentAuthorDto;
import pl.mateusz.example.friendoo.comment.PostCommentDto;
import pl.mateusz.example.friendoo.comment.PostCommentRepository;
import pl.mateusz.example.friendoo.comment.PostCommentService;
import pl.mateusz.example.friendoo.exceptions.AccessDeniedException;
import pl.mateusz.example.friendoo.exceptions.PageNotFoundException;
import pl.mateusz.example.friendoo.exceptions.UserNotFoundException;
import pl.mateusz.example.friendoo.exceptions.UserPostNotFoundException;
import pl.mateusz.example.friendoo.page.Page;
import pl.mateusz.example.friendoo.page.PageRepository;
import pl.mateusz.example.friendoo.photo.Photo;
import pl.mateusz.example.friendoo.photo.PhotoRepository;
import pl.mateusz.example.friendoo.post.page.PagePost;
import pl.mateusz.example.friendoo.post.postversion.PostVersionService;
import pl.mateusz.example.friendoo.post.user.UserPost;
import pl.mateusz.example.friendoo.post.user.UserPostRepository;
import pl.mateusz.example.friendoo.reaction.PostCommentReactionDto;
import pl.mateusz.example.friendoo.reaction.PostCommentReactionService;
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

  private final PostCommentService postCommentService;

  private final PostCommentReactionService postCommentReactionService;

  private final PostVersionService postVersionService;

  /**
   * Constructor for PostService.
   *
   * @param userPostRepository         repository for user posts
   * @param postReactionService        service for post reactions
   * @param userRepository             repository for users
   * @param postCommentRepository      repository for post comments
   * @param postReactionRepository     repository for post reactions
   * @param photoRepository            repository for photos
   * @param pageRepository             repository for pages
   * @param postCommentService         service for post comments
   * @param postCommentReactionService service for post comment reactions
   * @param postVersionService         service for post versions
   */
  public PostService(UserPostRepository userPostRepository, PostReactionService postReactionService,
                     UserRepository userRepository, PostCommentRepository postCommentRepository,
                     PostReactionRepository postReactionRepository, PhotoRepository photoRepository,
                     PageRepository pageRepository, PostCommentService postCommentService,
                     PostCommentReactionService postCommentReactionService, PostVersionService postVersionService) {
    this.userPostRepository = userPostRepository;
    this.postReactionService = postReactionService;
    this.userRepository = userRepository;
    this.postCommentRepository = postCommentRepository;
    this.postReactionRepository = postReactionRepository;
    this.photoRepository = photoRepository;
    this.pageRepository = pageRepository;
    this.postCommentService = postCommentService;
    this.postCommentReactionService = postCommentReactionService;
    this.postVersionService = postVersionService;
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
    Map<Long, Set<PostReactionDto>> reactionsMap = postReactionService
        .getReactionsForMultipleUserPosts(postIds);
    Map<Long, List<PostCommentDto>> commentMap = postCommentService
        .getCommentsForMultipleUserPosts(postIds);
    List<PostCommentDto> postComments = commentMap.values()
        .stream()
        .flatMap(List::stream)
        .toList();
    List<Long> postCommentsIds = postComments.stream()
        .map(PostCommentDto::getId)
        .toList();
    Map<Long, List<PostCommentReactionDto>> reactionsForMultipleUserPostComments =
        postCommentReactionService.getReactionsForMultipleUserPostComments(postCommentsIds);
    postComments.forEach(comment -> {
      List<PostCommentReactionDto> reactions = reactionsForMultipleUserPostComments
              .getOrDefault(comment.getId(), List.of());
      comment.setReactions(reactions);
    });

    return posts.stream()
      .map(post -> {
        MappablePost wrapper = new UserPostWrapper(post);
        return wrapper.toDto(reactionsMap.getOrDefault(post.getId(), Set.of()),
            commentMap.getOrDefault(post.getId(), List.of()));
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
    User loggedUser = userRepository.findUserByEmail(userEmail)
        .orElseThrow(() -> new UserNotFoundException("Nie znaleziono użytkownika"));
    UserPost userPost = new UserPost();
    userPost.setAuthor(loggedUser);
    userPost.setContent(postDto.getContent());
    userPost.setCreatedAt(LocalDateTime.now());
    UserPost saved = userPostRepository.save(userPost);
    MappablePost wrapper = new UserPostWrapper(saved);
    return wrapper.toDto(Collections.emptySet(), List.of());
  }

  /**
   * Retrieves a map of unique post comment authors by user posts.
   *
   * @param userPostsByAuthorId list of user posts
   * @return a map of post IDs to lists of unique post comment authors
   */
  public Map<Long, List<PostCommentAuthorDto>> getUniqueCommentAuthorsByPostId(List<PostDto>
                                                          userPostsByAuthorId) {
    return userPostsByAuthorId.stream()
      .collect(Collectors.toMap(
        PostDto::getId,
        post -> post.getComments().stream()
          .map(c -> new PostCommentAuthorDto(c.getUserAuthorId(), c.getAuthorFirstName(),
            c.getAuthorLastName()))
          .collect(Collectors.toMap(
            PostCommentAuthorDto::getId,
            Function.identity(),
            (a, b) -> a
          ))
          .values().stream().toList()
      ));
  }

  /**
   * Deletes a user post by its ID.
   *
   * @param userPostId      the ID of the user post to delete
   * @param authentication  the authentication object containing user details
   * @throws UserPostNotFoundException if the user post is not found
   * @throws AccessDeniedException     if the user does not have permission to delete the post
   */
  @Transactional
  public void deleteUserPost(Long userPostId, Authentication authentication) {
    UserPost userPost = userPostRepository.findById(userPostId)
        .orElseThrow(() -> new UserPostNotFoundException("Nie znaleziono postu użytkownika"));
    String currentlyLoggedUserEmail = ((UserDetails) authentication.getPrincipal()).getUsername();
    User currentlyLoggedUser = userRepository.findUserByEmail(currentlyLoggedUserEmail)
        .orElseThrow(() -> new UserNotFoundException("Nie znaleziono użytkownika"));
    if (userPost.getAuthor().getId().equals(currentlyLoggedUser.getId())) {
      userPostRepository.deleteById(userPostId);
    } else {
      throw new AccessDeniedException("Nie masz uprawnień do usunięcia tego postu");
    }
  }

  /**
   * Edits a user post by its ID.
   *
   * @param userPostId      the ID of the user post to edit
   * @param dto             the new content for the post
   * @param authentication  the authentication object containing user details
   * @return the edited post as {@link PostDto}
   * @throws UserNotFoundException     if the user is not found
   * @throws UserPostNotFoundException if the user post is not found
   * @throws AccessDeniedException     if the user does not have permission to edit the post
   */
  @Transactional
  public PostDto editUserPost(Long userPostId, PostEditDto dto,
                                            Authentication authentication) {
    String currentLoggedUserEmail = ((UserDetails) authentication.getPrincipal()).getUsername();
    User loggedUser = userRepository.findUserByEmail(currentLoggedUserEmail)
        .orElseThrow(() -> new UserNotFoundException("Nie znaleziono użytkownika"));
    UserPost userPost = userPostRepository.findById(userPostId)
        .orElseThrow(() -> new UserPostNotFoundException("Nie znaleziono posta"));
    postVersionService.createAndSavePostVersion(userPost, null, loggedUser.getId(),
        userPost.getContent());
    if (!userPost.getAuthor().getId().equals(loggedUser.getId())) {
      throw new AccessDeniedException("Brak autoryzacji do edytowania posta");
    }
    userPost.setContent(dto.getContent());
    userPost.setEditedAt(LocalDateTime.now());
    UserPost editedUserPost = userPostRepository.save(userPost);
    Map<Long, Set<PostReactionDto>> reactionsMap = postReactionService
        .getReactionsForMultipleUserPosts(List.of(userPostId));
    Map<Long, List<PostCommentDto>> commentsMap = postCommentService
        .getCommentsForMultipleUserPosts(List.of(userPostId));
    Set<PostReactionDto> reactions = reactionsMap.getOrDefault(userPostId, Set.of());
    List<PostCommentDto> comments = commentsMap.getOrDefault(userPostId, List.of());
    MappablePost wrapper = new UserPostWrapper(editedUserPost);
    return wrapper.toDto(reactions, comments);
  }

  private UserPost createUserPostEntity(PostDto postDto) {
    User user = userRepository.findById(postDto.getUserAuthorId())
        .orElseThrow(() -> new UserNotFoundException("Nie znaleziono użytkownika"));

    List<PostComment> comments = postCommentRepository.findAllByUserPostId(postDto.getId());
    Set<PostReaction> reactions = postReactionRepository.findAllByUserPostId(postDto.getId());
    List<Photo> photos = photoRepository.findAllByUserPostId(postDto.getId());

    return PostDtoMapper.mapFromDtoToUserPost(postDto, user, comments, reactions, photos);
  }

  private PagePost createPagePostEntity(PostDto postDto) {
    User user = userRepository.findById(postDto.getUserAuthorId())
        .orElseThrow(() -> new UserNotFoundException("Nie znaleziono użytkownika"));
    Page page = pageRepository.findById(postDto.getPageAuthorId())
        .orElseThrow(() -> new PageNotFoundException("Nie znaleziono strony"));
    List<PostComment> comments = postCommentRepository.findAllByPagePostId(postDto.getId());
    Set<PostReaction> reactions = postReactionRepository.findAllByUserPostId(postDto.getId());
    List<Photo> photos = photoRepository.findAllByPagePostId(postDto.getId());
    return PostDtoMapper.mapFromDtoToPagePost(postDto, page, user, comments, reactions, photos);
  }


}
