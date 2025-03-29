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
import lombok.Setter;
import pl.mateusz.example.friendoo.comment.Comment;
import pl.mateusz.example.friendoo.photo.PagePhoto;
import pl.mateusz.example.friendoo.reaction.page.PagePhotoReaction;
import pl.mateusz.example.friendoo.user.User;


/**
 * Entity representing comment under page photo.
 */
@Entity
@Getter
@Setter
public class PagePhotoComment extends Comment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "page_photo_id")
  private PagePhoto pagePhoto;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User author;

  @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
  private Set<PagePhotoReaction> reactions = new HashSet<>();

}
