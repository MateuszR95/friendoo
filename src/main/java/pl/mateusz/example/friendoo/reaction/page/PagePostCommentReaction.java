package pl.mateusz.example.friendoo.reaction.page;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.mateusz.example.friendoo.comment.page.PagePostComment;
import pl.mateusz.example.friendoo.reaction.CommentReaction;

@SuppressWarnings("checkstyle:MissingJavadocType")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
    uniqueConstraints = @UniqueConstraint(columnNames = {"page_post_comment_id", "author_id"})
)
public class PagePostCommentReaction extends CommentReaction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "page_post_comment_id")
  private PagePostComment comment;

}