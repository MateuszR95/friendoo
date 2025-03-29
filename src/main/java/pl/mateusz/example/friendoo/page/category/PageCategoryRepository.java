package pl.mateusz.example.friendoo.page.category;

import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for the PageCategory entity.
 */
public interface PageCategoryRepository extends JpaRepository<PageCategory, Long> {

  Set<PageCategory> findAllBy();

  Optional<PageCategory> findPageCategoryById(Long id);
}
