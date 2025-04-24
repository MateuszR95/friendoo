package pl.mateusz.example.friendoo.comment.user;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.mateusz.example.friendoo.page.Page;
import pl.mateusz.example.friendoo.post.page.PagePost;
import pl.mateusz.example.friendoo.post.user.UserPost;
import pl.mateusz.example.friendoo.reaction.PostCommentReaction;
import pl.mateusz.example.friendoo.user.User;

/**
 * Entity representing comment under user post.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class PostComment extends CommentEntry {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_post_id")
  private UserPost userPost;

  @ManyToOne
  @JoinColumn(name = "page_post_id")
  private PagePost pagePost;

  @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
  private Set<PostCommentReaction> reactions = new HashSet<>();

}
