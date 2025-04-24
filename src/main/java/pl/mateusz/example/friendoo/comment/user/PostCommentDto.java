package pl.mateusz.example.friendoo.comment.user;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object representing a comment on a user or page post.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostCommentDto {

  private Long id;
  private LocalDateTime createdAt;
  private String content;
  private Long userAuthorId;
  private Long pageAuthorId;
  private Long userPostId;
  private Long pagePostId;
  private String creationDateDescription;
  private int reactionsCount;

}
