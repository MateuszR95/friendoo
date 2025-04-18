package pl.mateusz.example.friendoo.comment.user;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.mateusz.example.friendoo.reaction.user.UserPostCommentReaction;

/**
 * Data Transfer Object for UserPostComment.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPostCommentDto {

  private Long id;
  private Long authorId;
  private Long postId;
  private LocalDateTime createdAt;
  private String creationDateDescription;
  private String content;
  private int reactionsCount;


}
