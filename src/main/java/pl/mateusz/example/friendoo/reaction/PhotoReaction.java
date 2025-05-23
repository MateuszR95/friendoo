package pl.mateusz.example.friendoo.reaction;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.mateusz.example.friendoo.photo.Photo;

/**
 * Entity representing a reaction to a photo.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class PhotoReaction extends ReactionEntry {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "photo_id")
  private Photo photo;

}
