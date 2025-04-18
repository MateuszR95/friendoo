package pl.mateusz.example.friendoo.reaction.user;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) representing a reaction to a user post comment.
 * This class is used to transfer data between the application layers.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserPostCommentReactionDto {

  private Long id;
  private Long commentId;
  private Long authorId;
  private Long reactionId;
  private LocalDateTime reactionTime;

}
