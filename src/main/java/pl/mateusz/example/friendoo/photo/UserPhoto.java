package pl.mateusz.example.friendoo.photo;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.mateusz.example.friendoo.comment.user.UserPhotoComment;
import pl.mateusz.example.friendoo.post.user.UserPost;
import pl.mateusz.example.friendoo.reaction.user.UserPhotoReaction;
import pl.mateusz.example.friendoo.user.User;

@SuppressWarnings("checkstyle:MissingJavadocType")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserPhoto extends Photo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne
  @JoinColumn(name = "user_post_id", nullable = true)
  private UserPost userPost;

  @OneToMany(mappedBy = "userPhoto", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<UserPhotoReaction> reactions = new HashSet<>();

  @OneToMany(mappedBy = "userPhoto", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<UserPhotoComment> comments = new HashSet<>();

}
