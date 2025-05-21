package pl.mateusz.example.friendoo.post;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for editing a post.
 * It contains the necessary fields to transfer post edit data.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostEditDto {

  @NotBlank
  private String content;
}
