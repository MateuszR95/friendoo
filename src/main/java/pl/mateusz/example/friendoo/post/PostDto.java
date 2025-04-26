package pl.mateusz.example.friendoo.post;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.mateusz.example.friendoo.reaction.PostReactionDto;
import pl.mateusz.example.friendoo.reaction.ReactionType;

/**
 * Data Transfer Object (DTO) representing a post.
 * It contains the necessary fields to transfer post data.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDto {

  private Long id;
  private Long userAuthorId;
  private Long pageAuthorId;
  @NotBlank
  private String content;
  private LocalDateTime createdAt;
  private String createdAtDisplay;
  private int reactionsCount;
  private int commentsCount;
  private List<PostReactionDto> reactions;
  private PostType postType;

  /**
   * Gets the reactions of the post grouped by reaction type.
   *
   * @return a map where the key is the reaction type and the value is a list of
   *         PostReactionDto objects representing the reactions of that type
   */
  public Map<ReactionType, List<PostReactionDto>> getPostReactionDtoByReactionType() {
    return reactions.stream()
    .collect(Collectors.groupingBy(PostReactionDto::getReactionType))
    .entrySet().stream()
    .sorted((s1, s2) -> Integer.compare(s2.getValue().size(), s1.getValue().size()))
    .collect(Collectors.toMap(
      Map.Entry::getKey,
      Map.Entry::getValue,
      (e1, e2) -> e1,
      LinkedHashMap::new));
  }

  /**
 * Checks if the post has been reacted to by the current logged-in user.
 *
 * @param id the ID of the current logged-in user
 * @return true if the post has been reacted to by the current logged-in
 *         user, false otherwise
 */
  public boolean isUserPostReactedByCurrentLoggedUserId(Long id) {
    return reactions.stream()
    .anyMatch(userPostReactionDto -> userPostReactionDto.getUserAuthorId()
      .equals(id));
  }

  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  public ReactionType getUserPostReactionTypeByCurrentLoggedUserId(Long id) {
    return reactions.stream()
    .filter(userPostReactionDto -> userPostReactionDto.getUserAuthorId()
      .equals(id))
    .map(PostReactionDto::getReactionType)
    .findFirst()
    .orElse(null);
  }
}