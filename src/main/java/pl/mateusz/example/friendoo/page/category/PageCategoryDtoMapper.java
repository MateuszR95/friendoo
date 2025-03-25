package pl.mateusz.example.friendoo.page.category;

@SuppressWarnings("checkstyle:MissingJavadocType")
public class PageCategoryDtoMapper {

  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  public static PageCategoryDto mapToPageCategoryDto(PageCategory pageCategory) {
    return PageCategoryDto.builder()
      .id(pageCategory.getId())
      .pageCategoryName(pageCategory.getPageCategoryType().getPlName())
      .build();
  }
}
