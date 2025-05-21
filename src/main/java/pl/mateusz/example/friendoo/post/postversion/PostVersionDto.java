package pl.mateusz.example.friendoo.post.postversion;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for PostVersion.
 * This class is used to transfer data related to post versions.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostVersionDto {

  private Long id;
  private String authorFirstName;
  private String authorLastName;
  private Long editorId;
  private String content;
  private LocalDateTime createdAt;

}
