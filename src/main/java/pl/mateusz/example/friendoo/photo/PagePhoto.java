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
import pl.mateusz.example.friendoo.comment.page.PagePhotoComment;
import pl.mateusz.example.friendoo.page.Page;
import pl.mateusz.example.friendoo.post.page.PagePost;
import pl.mateusz.example.friendoo.reaction.page.PagePhotoReaction;

/**
 * Entity representing a photo on a page.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class PagePhoto extends Photo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "page_id")
  private Page page;

  @OneToMany(mappedBy = "pagePhoto", cascade = CascadeType.ALL)
  private Set<PagePhotoReaction> reactions = new HashSet<>();

  @OneToMany(mappedBy = "pagePhoto", cascade = CascadeType.ALL)
  private Set<PagePhotoComment> comments = new HashSet<>();

  @ManyToOne
  @JoinColumn(name = "post_id")
  private PagePost pagePost;


}
