package pl.mateusz.example.friendoo.post.postversion;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.mateusz.example.friendoo.post.page.PagePost;
import pl.mateusz.example.friendoo.post.user.UserPost;

/**
 * Service class for managing post versions.
 * This class contains the business logic for handling post versions.
 */
@Service
public class PostVersionService {

  private final PostVersionRepository postVersionRepository;

  public PostVersionService(PostVersionRepository postVersionRepository) {
    this.postVersionRepository = postVersionRepository;
  }

  /**
   * Creates and saves a new post version.
   *
   * @param userPost      The user post associated with this version.
   * @param pagePost      The page post associated with this version.
   * @param editorUserId  The ID of the user who edited the post.
   * @param content       The content of the post version.
   */
  @Transactional
  public void createAndSavePostVersion(UserPost userPost, PagePost pagePost,
                                       Long editorUserId, String content) {
    PostVersion postVersion = new PostVersion();
    postVersion.setUserPost(userPost);
    postVersion.setPagePost(pagePost);
    postVersion.setEditorUserId(editorUserId);
    postVersion.setContent(content);
    if (userPost.getEditedAt() == null) {
      postVersion.setCreatedAt(userPost.getCreatedAt());
    } else {
      postVersion.setCreatedAt(userPost.getEditedAt());
    }
    postVersionRepository.save(postVersion);
  }

  /**
   * Retrieves all post versions associated with a specific user post ID.
   *
   * @param userPostId The ID of the user post.
   * @return A list of PostVersionDto objects representing the post versions.
   */
  public List<PostVersionDto> getPostVersionsByUserPostId(Long userPostId) {
    return postVersionRepository.findAllByUserPostId(userPostId)
        .stream()
        .map(PostVersionDtoMapper::mapToDto)
        .collect(Collectors.toList());
  }

  public Set<Long> getDistinctEditedUserPostIds() {
    return postVersionRepository.findDistinctUserPostIdsWithVersions();
  }
}
