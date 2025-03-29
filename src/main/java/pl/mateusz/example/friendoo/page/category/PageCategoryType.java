package pl.mateusz.example.friendoo.page.category;

import lombok.Getter;

/**
 * Enum representing page category type.
 */
@Getter
public enum PageCategoryType {
  BUSINESS("Biznes"),
  BRAND("Marka"),
  ARTIST("Artysta"),
  ENTERTAINMENT("Rozrywka"),
  SPORT("Sport"),
  PUBLIC_FIGURE("Osoba Publiczna"),
  COMMUNITY("Społeczność"),
  EDUCATION("Edukacja"),
  TECHNOLOGY("Technologia"),
  FASHION("Moda"),
  FOOD("Jedzenie"),
  TRAVEL("Podróże"),
  HEALTH("Zdrowie");

  private final String plName;

  PageCategoryType(String plName) {
    this.plName = plName;
  }

}
