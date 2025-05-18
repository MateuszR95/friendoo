package pl.mateusz.example.friendoo.reaction;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.mateusz.example.friendoo.comment.PhotoComment;

/**
 * Entity representing a reaction given to a comment on a user photo.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhotoCommentReaction extends ReactionEntry {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "comment_id")
  private PhotoComment comment;
}
