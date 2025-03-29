package pl.mateusz.example.friendoo.user.favouritepagecategory;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for {@link UserFavouritePageCategory} entities.
 */
public interface UserFavouritePageCategoryRepository extends
    JpaRepository<UserFavouritePageCategory, Long> {
}
