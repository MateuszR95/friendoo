package pl.mateusz.example.friendoo.reaction.user;

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
import pl.mateusz.example.friendoo.comment.user.UserPhotoComment;
import pl.mateusz.example.friendoo.photo.UserPhoto;
import pl.mateusz.example.friendoo.reaction.Reaction;
import pl.mateusz.example.friendoo.user.User;


@SuppressWarnings("checkstyle:MissingJavadocType")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserPhotoReaction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne
  @JoinColumn(name = "user_photo_id")
  private UserPhoto userPhoto;

  @ManyToOne
  @JoinColumn(name = "reaction_id")
  private Reaction reaction;

  @ManyToOne
  @JoinColumn(name = "comment_id")
  private UserPhotoComment comment;

  private LocalDateTime reactionTime;

}
