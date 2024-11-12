package pl.mateusz.example.friendoo.reaction;

@SuppressWarnings("checkstyle:MissingJavadocType")
public enum ReactionType {
  LIKE("Lubię to"),
  LOVE("Super"),
  HAHA("Ha ha"),
  WOW("Wow"),
  SAD("Przykro mi"),
  ANGRY("Wrr");
  private final String text;

  ReactionType(String text) {
    this.text = text;
  }
}
