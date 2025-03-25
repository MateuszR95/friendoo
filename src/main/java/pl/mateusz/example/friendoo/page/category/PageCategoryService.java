package pl.mateusz.example.friendoo.page.category;

import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@SuppressWarnings("checkstyle:MissingJavadocType")
@Service
public class PageCategoryService {

  private final PageCategoryRepository pageCategoryRepository;

  public PageCategoryService(PageCategoryRepository pageCategoryRepository) {
    this.pageCategoryRepository = pageCategoryRepository;
  }

  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  public TreeSet<PageCategoryDto> getAllPageCategories() {
    return pageCategoryRepository.findAllBy().stream()
        .map(PageCategoryDtoMapper::mapToPageCategoryDto)
        .collect(Collectors.toCollection(TreeSet::new));
  }

}
