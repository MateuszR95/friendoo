package pl.mateusz.example.friendoo.reaction;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for post reaction.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostReactionDto {

  private Long id;
  private Long userAuthorId;
  private Long pageAuthorId;
  private Long pagePostId;
  private Long userPostId;
  private String authorFirstName;
  private String authorLastName;
  private String pageName;
  private ReactionType reactionType;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime reactionTime;

}
