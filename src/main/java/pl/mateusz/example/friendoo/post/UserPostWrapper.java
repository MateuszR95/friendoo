package pl.mateusz.example.friendoo.post;

import java.util.List;
import java.util.Set;
import pl.mateusz.example.friendoo.comment.PostCommentDto;
import pl.mateusz.example.friendoo.post.user.UserPost;
import pl.mateusz.example.friendoo.reaction.PostReactionDto;


@SuppressWarnings("checkstyle:MissingJavadocType")
public class UserPostWrapper implements MappablePost {
  private final UserPost userPost;

  public UserPostWrapper(UserPost userPost) {
    this.userPost = userPost;
  }

  @Override
  public PostDto toDto(Set<PostReactionDto> reactions, List<PostCommentDto> comments) {
    return PostDto.builder()
      .id(userPost.getId())
      .userAuthorId(userPost.getAuthor().getId())
      .pageAuthorId(null)
      .content(userPost.getContent())
      .createdAt(userPost.getCreatedAt())
      .createdAtDisplay(PostDateFormatter.updatePostDateDisplay(userPost.getCreatedAt()))
      .postType(PostType.USER_POST)
      .reactions(reactions)
      .comments(comments)
      .reactionsCount(reactions.size())
      .commentsCount(comments.size())
      .build();
  }
}
