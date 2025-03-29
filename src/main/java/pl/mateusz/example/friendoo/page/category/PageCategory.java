package pl.mateusz.example.friendoo.page.category;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.mateusz.example.friendoo.page.Page;

/**
 * Entity representing a category of pages.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class PageCategory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  private PageCategoryType pageCategoryType;

  @OneToMany(mappedBy = "pageCategory", cascade = CascadeType.ALL)
  private Set<Page> pages = new HashSet<>();

}
