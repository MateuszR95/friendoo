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
import pl.mateusz.example.friendoo.comment.PhotoComment;
import pl.mateusz.example.friendoo.page.Page;
import pl.mateusz.example.friendoo.post.page.PagePost;
import pl.mateusz.example.friendoo.post.user.UserPost;
import pl.mateusz.example.friendoo.reaction.PhotoReaction;
import pl.mateusz.example.friendoo.user.User;

/**
 * Entity representing a photo.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Photo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "page_id")
  private Page page;

  @ManyToOne
  @JoinColumn(name = "page_post_id")
  private PagePost pagePost;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne
  @JoinColumn(name = "user_post_id")
  private UserPost userPost;

  @OneToMany(mappedBy = "photo", cascade = CascadeType.ALL)
  private Set<PhotoReaction> reactions = new HashSet<>();

  @OneToMany(mappedBy = "photo", cascade = CascadeType.ALL)
  private Set<PhotoComment> comments = new HashSet<>();

}
