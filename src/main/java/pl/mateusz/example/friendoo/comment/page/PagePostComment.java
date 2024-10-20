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
import pl.mateusz.example.friendoo.reaction.page.PagePostReaction;
import pl.mateusz.example.friendoo.user.User;

@SuppressWarnings("checkstyle:MissingJavadocType")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class PagePostComment extends Comment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User author;

  @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
  private Set<PagePostReaction> reactions = new HashSet<>();

  @ManyToOne
  @JoinColumn(name = "post_id")
  private PagePost post;

}
