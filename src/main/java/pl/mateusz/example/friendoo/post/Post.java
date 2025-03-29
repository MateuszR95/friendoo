package pl.mateusz.example.friendoo.post;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Abstract class representing a post.
 */
@Getter
@Setter
@NoArgsConstructor
public abstract class Post {

  private LocalDateTime createdAt;

  private String content;

}
