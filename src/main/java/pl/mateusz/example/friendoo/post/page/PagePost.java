package pl.mateusz.example.friendoo.post.page;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.mateusz.example.friendoo.comment.user.PostComment;
import pl.mateusz.example.friendoo.page.Page;
import pl.mateusz.example.friendoo.photo.Photo;
import pl.mateusz.example.friendoo.post.Post;
import pl.mateusz.example.friendoo.reaction.PostReaction;
import pl.mateusz.example.friendoo.user.User;

/**
 * Entity representing a post on a page.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagePost extends Post {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "page_id")
  private Page page;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User pageAdminUser;

  @OneToMany(mappedBy = "pagePost", cascade = CascadeType.ALL)
  private Set<PostReaction> reactions = new HashSet<>();

  @OneToMany(mappedBy = "pagePost", cascade = CascadeType.ALL)
  private Set<PostComment> comments = new HashSet<>();

  @OneToMany(mappedBy = "pagePost", cascade = CascadeType.ALL)
  private List<Photo> photos = new ArrayList<>();
}
