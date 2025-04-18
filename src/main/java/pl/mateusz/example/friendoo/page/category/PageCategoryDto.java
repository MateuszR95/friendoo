package pl.mateusz.example.friendoo.page.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO representing a category of pages.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageCategoryDto implements Comparable<PageCategoryDto> {

  private Long id;
  private String pageCategoryName;

  @Override
  public int compareTo(PageCategoryDto pageCategoryDto) {
    return id.compareTo(pageCategoryDto.id);
  }
}
