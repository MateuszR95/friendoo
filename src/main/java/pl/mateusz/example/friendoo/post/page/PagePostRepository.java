package pl.mateusz.example.friendoo.post.page;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing PagePost entities.
 * It extends JpaRepository to provide CRUD operations and query methods.
 */
public interface PagePostRepository extends JpaRepository<PagePost, Long> {

}
