package pl.mateusz.example.friendoo.comment;

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.mateusz.example.friendoo.page.Page;
import pl.mateusz.example.friendoo.user.User;

/**
 * Abstract class representing a comment entry.
 */
@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class CommentEntry {

  private LocalDateTime createdAt;

  private String content;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "author_id")
  private User author;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "page_author_id")
  private Page pageAuthor;

  private LocalDateTime editedAt;

}
