package pl.mateusz.example.friendoo.photo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing Photo entities.
 * Extends JpaRepository to provide CRUD operations and database interaction
 * for the Photo entity.
 */
public interface PhotoRepository extends JpaRepository<Photo, Long> {

  List<Photo> findAllByUserPostId(Long id);

  List<Photo> findAllByPagePostId(Long id);
}
