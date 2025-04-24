package pl.mateusz.example.friendoo.post;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SuppressWarnings("checkstyle:MissingJavadocType")
public class PostDateFormatter {

  /**
   * Formats the given date to a user-friendly string:
   * "Dzisiaj" if the date is today.
   * "Wczoraj" if the date is yesterday.
   * "dd-MM-yyyy" format otherwise.
   *
   * @param createdAt the date to format
   * @return formatted date string
   */
  public static String updatePostDateDisplay(LocalDateTime createdAt) {
    LocalDateTime now = LocalDateTime.now();
    if (createdAt.toLocalDate().isEqual(LocalDateTime.now().toLocalDate())) {
      return "Dzisiaj";
    } else if (createdAt.toLocalDate().isEqual(now.minusDays(1).toLocalDate())) {
      return "Wczoraj";
    } else {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
      return createdAt.toLocalDate().format(formatter);
    }
  }
}
