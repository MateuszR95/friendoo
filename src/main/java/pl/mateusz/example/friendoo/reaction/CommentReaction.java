package pl.mateusz.example.friendoo.reaction;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import pl.mateusz.example.friendoo.user.User;

@SuppressWarnings("checkstyle:MissingJavadocType")
@MappedSuperclass
public abstract class CommentReaction {
  @ManyToOne
  @JoinColumn(name = "author_id")
  private User author;
  @ManyToOne
  @JoinColumn(name = "reaction_id")
  private Reaction reaction;
  private LocalDateTime reactionTime;


}
