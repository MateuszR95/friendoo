package pl.mateusz.example.friendoo.reaction;

/**
 * Enum representing different types of reactions.
 */
public enum ReactionType {
  LIKE("Lubię to"),
  LOVE("Super"),
  HAHA("Ha ha"),
  WOW("Wow"),
  SAD("Przykro mi"),
  ANGRY("Wrr");
  private final String plName;

  ReactionType(String text) {
    this.plName = text;
  }
}
