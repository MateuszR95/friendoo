package pl.mateusz.example.friendoo.page.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("checkstyle:MissingJavadocType")
@Getter
@Setter
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
