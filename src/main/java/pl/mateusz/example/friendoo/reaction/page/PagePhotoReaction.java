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
import pl.mateusz.example.friendoo.comment.page.PagePhotoComment;
import pl.mateusz.example.friendoo.photo.PagePhoto;
import pl.mateusz.example.friendoo.reaction.Reaction;
import pl.mateusz.example.friendoo.user.User;

/**
 * Entity representing a reaction given to a photo on a page.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class PagePhotoReaction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "page_photo_id")
  private PagePhoto pagePhoto;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne
  @JoinColumn(name = "reaction_id")
  private Reaction reaction;

  @ManyToOne
  @JoinColumn(name = "comment_id")
  private PagePhotoComment comment;

  private LocalDateTime reactionTime;


}
