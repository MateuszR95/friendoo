package pl.mateusz.example.friendoo.page;

import org.springframework.data.jpa.repository.JpaRepository;

@SuppressWarnings("checkstyle:MissingJavadocType")
public interface PageRepository extends JpaRepository<Page, Long> {
}
