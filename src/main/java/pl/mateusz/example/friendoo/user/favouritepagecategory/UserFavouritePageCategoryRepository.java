package pl.mateusz.example.friendoo.user.favouritepagecategory;

import org.springframework.data.jpa.repository.JpaRepository;

@SuppressWarnings("checkstyle:MissingJavadocType")
public interface UserFavouritePageCategoryRepository extends
    JpaRepository<UserFavouritePageCategory, Long> {
}
