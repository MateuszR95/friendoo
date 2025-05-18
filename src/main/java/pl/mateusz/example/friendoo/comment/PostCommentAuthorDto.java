package pl.mateusz.example.friendoo.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object representing the author of a post comment.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostCommentAuthorDto {

  private Long id;
  private String firstName;
  private String lastName;


}
