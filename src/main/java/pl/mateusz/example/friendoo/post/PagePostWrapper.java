package pl.mateusz.example.friendoo.post;

import java.util.List;
import java.util.Set;
import pl.mateusz.example.friendoo.comment.PostCommentDto;
import pl.mateusz.example.friendoo.post.page.PagePost;
import pl.mateusz.example.friendoo.reaction.PostReactionDto;

/**
 * Wrapper class for PagePost that implements MappablePost interface.
 * This class is responsible for converting a PagePost to its corresponding Data Transfer
 * Object (DTO)
 */
public class PagePostWrapper implements MappablePost {

  private final PagePost pagePost;

  public PagePostWrapper(PagePost pagePost) {
    this.pagePost = pagePost;
  }

  @Override
  public PostDto toDto(Set<PostReactionDto> reactions, List<PostCommentDto> comments) {
    return PostDto.builder()
      .id(pagePost.getId())
      .userAuthorId(pagePost.getPageAdminUser().getId())
      .pageAuthorId(pagePost.getPage().getId())
      .content(pagePost.getContent())
      .createdAt(pagePost.getCreatedAt())
      .createdAtDisplay(PostDateFormatter.updatePostDateDisplay(pagePost.getCreatedAt()))
      .reactionsCount(pagePost.getReactions().size())
      .commentsCount(pagePost.getComments().size())
      .reactions(reactions)
      .comments(comments)
      .postType(PostType.PAGE_POST)
      .build();
  }
}
