package pl.mateusz.example.friendoo.post.user;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import pl.mateusz.example.friendoo.reaction.user.UserPostReactionDto;

/**
 * Mapper class for converting `UserPost` entities to `UserPostDto` objects.
 * This class is responsible for transforming the data between the entity and DTO layers.
 */
public class UserPostDtoMapper {

  /**
   * Converts a UserPost entity to a UserPostDto.
   *
   * @param userPost the UserPost entity to convert
   * @return the converted UserPostDto
   */
  public static UserPostDto mapToUserPostDto(UserPost userPost, List<UserPostReactionDto>
        userPostReactions) {
    return UserPostDto.builder()
        .id(userPost.getId())
        .authorId(userPost.getAuthor().getId())
        .content(userPost.getContent())
        .createdAt(userPost.getCreatedAt())
        .createdAtDisplay(updatePostDateDisplay(userPost.getCreatedAt()))
        .reactionsCount(userPost.getReactions().size())
        .commentsCount(userPost.getComments().size())
        .reactions(userPostReactions)
        .build();

  }

  /**
   * Updates the display format of the post creation date.
   * If the post was created today, it returns "Dzisiaj" (Today in Polish).
   * If the post was created yesterday, it returns "Wczoraj" (Yesterday in Polish).
   * Otherwise, it formats the date as "dd-MM-yyyy".
   *
   * @param createdAt the creation date and time of the post
   * @return a string representing the formatted creation date
   */
  private static String updatePostDateDisplay(LocalDateTime createdAt) {
    LocalDateTime now = LocalDateTime.now();
    if (createdAt.toLocalDate().isEqual(now.toLocalDate())) {
      return "Dzisiaj";
    } else if (createdAt.toLocalDate().isEqual(now.minusDays(1).toLocalDate())) {
      return "Wczoraj";
    } else {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
      return createdAt.toLocalDate().format(formatter);
    }
  }
}
