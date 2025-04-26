package pl.mateusz.example.friendoo.comment.user;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
import pl.mateusz.example.friendoo.post.page.PagePost;
import pl.mateusz.example.friendoo.post.user.UserPost;
import pl.mateusz.example.friendoo.reaction.PostCommentReaction;


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

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_post_id")
  private UserPost userPost;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "page_post_id")
  private PagePost pagePost;

  @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
  private Set<PostCommentReaction> reactions = new HashSet<>();

}
