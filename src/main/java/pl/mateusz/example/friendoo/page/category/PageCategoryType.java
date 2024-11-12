package pl.mateusz.example.friendoo.page.category;

@SuppressWarnings("checkstyle:MissingJavadocType")
public enum PageCategoryType {
  BUSINESS("Firma/Organizacja"),
  BRAND("Marka/Produkt"),
  ARTIST("Artysta/Zespół/Muzyk"),
  ENTERTAINMENT("Rozrywka"),
  SPORT("Sporty/Rekreacja"),
  PUBLIC_FIGURE("Osoba Publiczna"),
  COMMUNITY("Społeczność"),
  EDUCATION("Edukacja"),
  TECHNOLOGY("Technologia"),
  FASHION("Moda"),
  FOOD("Jedzenie"),
  TRAVEL("Podróże"),
  HEALTH("Zdrowie");

  private String text;

  PageCategoryType(String text) {
    this.text = text;
  }
}
