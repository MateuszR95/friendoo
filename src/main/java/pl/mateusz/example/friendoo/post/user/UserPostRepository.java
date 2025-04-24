package pl.mateusz.example.friendoo.post.user;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing UserPost entities.
 * It extends JpaRepository to provide CRUD operations and query methods.
 */
public interface UserPostRepository extends JpaRepository<UserPost, Long> {
  @EntityGraph(attributePaths = {
    "author",
    "reactions",
    "comments",
    "author.roles",
    "author.gender",
    "author.hometown",
    "author.currentCity"
  })
  List<UserPost> findUserPostsByAuthorIdOrderByCreatedAtDesc(Long authorId);

}
