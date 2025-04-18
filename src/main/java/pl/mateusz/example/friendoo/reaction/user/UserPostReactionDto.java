package pl.mateusz.example.friendoo.reaction.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.mateusz.example.friendoo.reaction.ReactionType;

/**
 * Data Transfer Object (DTO) representing a reaction to a user post.
 * This class is used to transfer data between the application layers.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserPostReactionDto {

  private Long id;
  private Long authorId;
  private Long userPostId;
  private String authorFirstName;
  private String authorLastName;
  private ReactionType reactionType;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime reactionTime;

}
