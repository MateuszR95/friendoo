package pl.mateusz.example.friendoo.post;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@SuppressWarnings("checkstyle:MissingJavadocType")
@Getter
@Setter
@NoArgsConstructor
public abstract class Post {

  private LocalDateTime createdAt;

  private String content;

}
