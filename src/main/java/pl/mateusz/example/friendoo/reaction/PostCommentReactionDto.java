package pl.mateusz.example.friendoo.reaction;


import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for post comment reaction.
 * It contains the necessary fields to transfer post comment reaction data.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostCommentReactionDto {

  private Long id;
  private Long userAuthorId;
  private String userAuthorFirstName;
  private String userAuthorLastName;
  private Long pageAuthorId;
  private ReactionType reactionType;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime reactionTime;
  private Long postCommentId;

}
