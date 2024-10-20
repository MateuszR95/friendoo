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
import pl.mateusz.example.friendoo.comment.page.PagePostComment;
import pl.mateusz.example.friendoo.post.page.PagePost;
import pl.mateusz.example.friendoo.reaction.Reaction;
import pl.mateusz.example.friendoo.user.User;


@SuppressWarnings("checkstyle:MissingJavadocType")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class PagePostReaction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "page_post_id")
  private PagePost post;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne
  @JoinColumn(name = "reaction_id")
  private Reaction reaction;

  @ManyToOne
  @JoinColumn(name = "comment_id")
  private PagePostComment comment;

  private LocalDateTime reactionTime;


}
