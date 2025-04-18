package pl.mateusz.example.friendoo.reaction;

import lombok.Getter;

/**
 * Enum representing different types of reactions.
 */
@Getter
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

  /**
   * Returns the emoji associated with the reaction type.
   *
   * @return the emoji as a string
   */
  public String getEmoji() {
    return switch (this) {
      case LIKE -> "👍";
      case LOVE -> "❤️";
      case HAHA -> "😆";
      case WOW -> "😮";
      case SAD -> "😢";
      case ANGRY -> "😡";
      default -> "";
    };
  }

}
