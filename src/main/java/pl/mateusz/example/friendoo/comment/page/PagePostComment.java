package pl.mateusz.example.friendoo.comment.page;

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
import pl.mateusz.example.friendoo.post.page.PagePost;
import pl.mateusz.example.friendoo.reaction.page.PagePostCommentReaction;
import pl.mateusz.example.friendoo.user.User;

/**
 * Entity representing comment under page post.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class PagePostComment extends Comment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "author_id")
  private User author;
  
  @ManyToOne
  @JoinColumn(name = "page_post_id")
  private PagePost post;

  @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
  private Set<PagePostCommentReaction> reactions = new HashSet<>();

}
