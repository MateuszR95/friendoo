package pl.mateusz.example.friendoo.page.category;

import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

@SuppressWarnings("checkstyle:MissingJavadocType")
public interface PageCategoryRepository extends JpaRepository<PageCategory, Long> {

  Set<PageCategory> findAllBy();

  Optional<PageCategory> findById(Long id);
}
