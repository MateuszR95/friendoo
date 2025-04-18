package pl.mateusz.example.friendoo.post.user;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.mateusz.example.friendoo.reaction.ReactionType;
import pl.mateusz.example.friendoo.reaction.user.UserPostReactionDto;

/**
 * Data Transfer Object (DTO) representing a user post.
 * It contains the necessary fields to transfer user post data.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserPostDto {
  private Long id;
  private Long authorId;
  private String content;
  private LocalDateTime createdAt;
  private String createdAtDisplay;
  private int reactionsCount;
  private int commentsCount;
  private List<UserPostReactionDto> reactions;

  /**
   * Groups the reactions by their reaction type and sorts them in descending order
   * based on the number of reactions for each type.
   *
   * @return a map where the key is the reaction type and the value is a list of
   *         `UserPostReactionDto` objects associated with that reaction type,
   *         sorted by the size of the lists in descending order.
   */
  public Map<ReactionType, List<UserPostReactionDto>> getUserPostReactionDtoByReactionType() {
    return reactions.stream()
      .collect(Collectors.groupingBy(UserPostReactionDto::getReactionType))
      .entrySet().stream()
      .sorted((s1, s2) -> Integer.compare(s2.getValue().size(), s1.getValue().size()))
      .collect(Collectors.toMap(
        Map.Entry::getKey,
        Map.Entry::getValue,
        (e1, e2) -> e1,
        LinkedHashMap::new));
  }

  /**
   * Checks if the user post has been reacted to by the current logged-in user.
   *
   * @param id the ID of the current logged-in user
   * @return true if the user post has been reacted to by the current logged-in
   *         user, false otherwise
   */
  public boolean isUserPostReactedByCurrentLoggedUserId(Long id) {
    return reactions.stream()
      .anyMatch(userPostReactionDto -> userPostReactionDto.getAuthorId()
        .equals(id));
  }

  /**
   * Gets the reaction type of the user post by the current logged-in user.
   *
   * @param id the ID of the current logged-in user
   * @return the reaction type of the user post by the current logged-in user, or
   *         null if no reaction exists
   */
  public ReactionType getUserPostReactionTypeByCurrentLoggedUserId(Long id) {
    return reactions.stream()
      .filter(userPostReactionDto -> userPostReactionDto.getAuthorId()
        .equals(id))
      .map(UserPostReactionDto::getReactionType)
      .findFirst()
      .orElse(null);
  }

}
