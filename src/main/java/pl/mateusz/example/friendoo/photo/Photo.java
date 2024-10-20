package pl.mateusz.example.friendoo.photo;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@SuppressWarnings("checkstyle:MissingJavadocType")
@Getter
@Setter
@NoArgsConstructor
public abstract class Photo {

  private String photoUrl;
  private LocalDateTime photoUploadedAt;
  private String description;


}
