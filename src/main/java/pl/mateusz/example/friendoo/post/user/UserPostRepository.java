package pl.mateusz.example.friendoo.post.user;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing UserPost entities.
 * It extends JpaRepository to provide CRUD operations and query methods.
 */
public interface UserPostRepository extends JpaRepository<UserPost, Long> {

  List<UserPost> findUserPostsByAuthorIdOrderByCreatedAtDesc(Long authorId);

  Optional<UserPost> findUserPostById(Long id);
}
