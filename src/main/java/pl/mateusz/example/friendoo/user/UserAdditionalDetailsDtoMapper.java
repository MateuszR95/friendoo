package pl.mateusz.example.friendoo.user;

import java.util.Set;
import java.util.stream.Collectors;
import pl.mateusz.example.friendoo.page.category.PageCategory;
import pl.mateusz.example.friendoo.user.favouritepagecategory.UserFavouritePageCategory;

/**
 * Mapper for user additional details DTO.
 */
public class UserAdditionalDetailsDtoMapper {

  static UserAdditionalDetailsDto mapToUserAdditionalDetailsDto(User user) {
    String bio = user.getBio();
    String currentCity = user.getCurrentCity();
    String hometown = user.getHometown();
    String phoneNumber = user.getPhoneNumber();
    String email = user.getEmail();
    Set<Long> favouritePageCategoryIds = user.getFavouritePageCategories().stream()
        .map(UserFavouritePageCategory::getPageCategory).collect(Collectors.toSet())
        .stream()
        .map(PageCategory::getId)
        .collect(Collectors.toSet());
    return new UserAdditionalDetailsDto(email, bio, currentCity, hometown, phoneNumber,
      favouritePageCategoryIds);
  }
}
