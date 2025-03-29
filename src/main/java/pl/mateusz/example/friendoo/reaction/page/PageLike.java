package pl.mateusz.example.friendoo.reaction.page;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.mateusz.example.friendoo.page.Page;
import pl.mateusz.example.friendoo.user.User;

/**
 * Entity representing a like given to a page.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class PageLike {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "page_id", nullable = false)
  private Page page;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  private LocalDateTime likeTime;

}
