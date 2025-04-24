package pl.mateusz.example.friendoo.reaction;

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
import pl.mateusz.example.friendoo.page.Page;
import pl.mateusz.example.friendoo.post.page.PagePost;
import pl.mateusz.example.friendoo.post.user.UserPost;
import pl.mateusz.example.friendoo.user.User;

/**
 * Entity representing a reaction given to a post on a page.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "post_reaction")
public class PostReaction extends ReactionEntry {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "page_post_id")
  private PagePost pagePost;

  @ManyToOne
  @JoinColumn(name = "user_post_id")
  private UserPost userPost;

}
