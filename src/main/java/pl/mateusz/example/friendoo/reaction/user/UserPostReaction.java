package pl.mateusz.example.friendoo.reaction.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.mateusz.example.friendoo.post.user.UserPost;
import pl.mateusz.example.friendoo.reaction.Reaction;
import pl.mateusz.example.friendoo.user.User;

/**
 * Entity representing a reaction given to a post uploaded by a user.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
    uniqueConstraints = @UniqueConstraint(columnNames = {"author_id", "user_post_id"})
)
public class UserPostReaction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "author_id")
  private User user;

  @ManyToOne
  @JoinColumn(name = "user_post_id")
  private UserPost post;

  @ManyToOne
  @JoinColumn(name = "reaction_id")
  private Reaction reaction;

  private LocalDateTime reactionTime;


}
