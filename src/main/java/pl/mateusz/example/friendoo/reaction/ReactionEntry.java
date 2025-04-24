package pl.mateusz.example.friendoo.reaction;

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
 * Abstract class representing a reaction given to a comment.
 */
@MappedSuperclass
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class ReactionEntry {

  @ManyToOne
  @JoinColumn(name = "author_id")
  private User author;
  @ManyToOne
  @JoinColumn(name = "page_author_id")
  private Page pageAuthor;
  @ManyToOne
  @JoinColumn(name = "reaction_id")
  private Reaction reaction;
  private LocalDateTime reactionTime;
}
