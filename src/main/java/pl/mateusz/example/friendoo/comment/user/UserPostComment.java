package pl.mateusz.example.friendoo.comment.user;

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
import pl.mateusz.example.friendoo.comment.Comment;
import pl.mateusz.example.friendoo.post.user.UserPost;
import pl.mateusz.example.friendoo.reaction.user.UserPostCommentReaction;
import pl.mateusz.example.friendoo.user.User;

/**
 * Entity representing comment under user post.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserPostComment extends Comment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "author_id")
  private User author;

  @ManyToOne
  @JoinColumn(name = "post_id")
  private UserPost post;

  @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
  private Set<UserPostCommentReaction> reactions = new HashSet<>();

}
