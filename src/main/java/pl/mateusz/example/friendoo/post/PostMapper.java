package pl.mateusz.example.friendoo.post;

import java.util.List;
import java.util.Set;
import pl.mateusz.example.friendoo.comment.user.PostComment;
import pl.mateusz.example.friendoo.page.Page;
import pl.mateusz.example.friendoo.photo.Photo;
import pl.mateusz.example.friendoo.post.page.PagePost;
import pl.mateusz.example.friendoo.post.user.UserPost;
import pl.mateusz.example.friendoo.reaction.PostReaction;
import pl.mateusz.example.friendoo.user.User;

/**
 * Mapper class for converting between PostDto and Post entities.
 * This class provides methods to map data between the DTO and entity classes.
 */
public class PostMapper {

  /**
   * Maps a PostDto object to a UserPost object.
   *
   * @param dto the PostDto object to be mapped
   * @return the mapped UserPost object
   */
  public static UserPost mapFromDtoToUserPost(PostDto dto, User user, Set<PostComment> comments,
                                              Set<PostReaction> reactions,
                                              List<Photo> userPostPhotos) {
    UserPost userPost = new UserPost();
    userPost.setId(dto.getId());
    userPost.setAuthor(user);
    userPost.setContent(dto.getContent());
    userPost.setComments(comments);
    userPost.setReactions(reactions);
    userPost.setUserPostPhoto(userPostPhotos);
    userPost.setCreatedAt(dto.getCreatedAt());
    return userPost;
  }

  /**
   * Maps a PostDto object to a PagePost object.
   *
   * @param dto the PostDto object to be mapped
   * @return the mapped PagePost object
   */
  public static PagePost mapFromDtoToPagePost(PostDto dto, Page page, User postCreator,
                                              Set<PostComment> comments,
                                              Set<PostReaction> reactions,
                                              List<Photo> pagePostPhotos) {
    PagePost pagePost = new PagePost();
    pagePost.setId(dto.getId());
    pagePost.setPage(page);
    pagePost.setPageAdminUser(postCreator);
    pagePost.setReactions(reactions);
    pagePost.setComments(comments);
    pagePost.setPhotos(pagePostPhotos);
    pagePost.setCreatedAt(dto.getCreatedAt());
    pagePost.setContent(dto.getContent());
    return pagePost;
  }
}
