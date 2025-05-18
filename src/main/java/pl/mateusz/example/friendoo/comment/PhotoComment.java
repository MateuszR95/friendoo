package pl.mateusz.example.friendoo.comment;

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
import pl.mateusz.example.friendoo.photo.Photo;
import pl.mateusz.example.friendoo.reaction.PhotoCommentReaction;
import pl.mateusz.example.friendoo.reaction.PhotoReaction;

/**
 * Entity representing comment under user photo.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class PhotoComment extends CommentEntry {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "photo_id")
  private Photo photo;

  @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
  private Set<PhotoCommentReaction> reactions = new HashSet<>();

}
