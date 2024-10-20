package pl.mateusz.example.friendoo.post.user;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.mateusz.example.friendoo.comment.user.UserPostComment;
import pl.mateusz.example.friendoo.photo.UserPhoto;
import pl.mateusz.example.friendoo.post.Post;
import pl.mateusz.example.friendoo.reaction.user.UserPostReaction;
import pl.mateusz.example.friendoo.user.User;

@SuppressWarnings("checkstyle:MissingJavadocType")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserPost extends Post {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User author;

  @OneToMany(mappedBy = "userPost", cascade = CascadeType.ALL)
  private List<UserPhoto> userPostPhoto = new ArrayList<>();

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
  private Set<UserPostReaction> reactions = new HashSet<>();

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
  private Set<UserPostComment> comments = new HashSet<>();

}
