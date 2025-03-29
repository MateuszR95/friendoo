package pl.mateusz.example.friendoo.page.category;

/**
 * Mapper class for converting PageCategory entities to PageCategoryDto objects.
 */
public class PageCategoryDtoMapper {

  /**
   * Maps a PageCategory entity to a PageCategoryDto.
   *
   * @param pageCategory the PageCategory entity to map
   * @return the mapped PageCategoryDto
   */
  public static PageCategoryDto mapToPageCategoryDto(PageCategory pageCategory) {
    return PageCategoryDto.builder()
      .id(pageCategory.getId())
      .pageCategoryName(pageCategory.getPageCategoryType().getPlName())
      .build();
  }
}
