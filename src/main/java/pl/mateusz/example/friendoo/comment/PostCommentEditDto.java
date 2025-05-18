package pl.mateusz.example.friendoo.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for editing a post comment.
 * Contains the content of the comment to be edited.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostCommentEditDto {

  @NotBlank
  private String content;
}
