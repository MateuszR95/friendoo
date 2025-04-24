package pl.mateusz.example.friendoo.post;

import java.util.List;
import pl.mateusz.example.friendoo.post.user.UserPost;
import pl.mateusz.example.friendoo.reaction.PostReactionDto;


@SuppressWarnings("checkstyle:MissingJavadocType")
public class UserPostWrapper implements MappablePost {
  private final UserPost userPost;

  public UserPostWrapper(UserPost userPost) {
    this.userPost = userPost;
  }

  @Override
  public PostDto toDto(List<PostReactionDto> reactions) {
    return PostDto.builder()
      .id(userPost.getId())
      .userAuthorId(userPost.getAuthor().getId())
      .pageAuthorId(null)
      .content(userPost.getContent())
      .createdAt(userPost.getCreatedAt())
      .createdAtDisplay(PostDateFormatter.updatePostDateDisplay(userPost.getCreatedAt()))
      .postType(PostType.USER_POST)
      .reactions(reactions)
      .reactionsCount(userPost.getReactions().size())
      .commentsCount(userPost.getComments().size())
      .build();
  }
}
