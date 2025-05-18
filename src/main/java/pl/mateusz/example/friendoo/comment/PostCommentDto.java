package pl.mateusz.example.friendoo.comment;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.mateusz.example.friendoo.reaction.PostCommentReactionDto;
import pl.mateusz.example.friendoo.reaction.ReactionType;

/**
 * Data Transfer Object representing a comment on a user or page post.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostCommentDto {

  private Long id;
  private LocalDateTime createdAt;
  @NotBlank
  private String content;
  private String authorFirstName;
  private String authorLastName;
  private Long userAuthorId;
  private Long pageAuthorId;
  private Long userPostId;
  private Long pagePostId;
  private String creationDateDescription;
  private int reactionsCount;
  private List<PostCommentReactionDto> reactions;
  private Long quotedCommentId;
  private String quotedContent;
  private String quotedAuthorFirstName;
  private String quotedAuthorLastName;
  private LocalDateTime editedAt;


  /**
   * Gets the reactions of the post comment grouped by reaction type.
   *
   * @return a map where the key is the reaction type and the value is a list of
   *         PostCommentReactionDto objects representing the reactions of that type
   */
  public Map<ReactionType, List<PostCommentReactionDto>> getPostReactionDtoByReactionType() {
    return reactions.stream()
      .collect(Collectors.groupingBy(PostCommentReactionDto::getReactionType))
      .entrySet().stream()
      .sorted((s1, s2) -> Integer.compare(s2.getValue().size(), s1.getValue().size()))
      .collect(Collectors.toMap(
        Map.Entry::getKey,
        Map.Entry::getValue,
        (e1, e2) -> e1,
        LinkedHashMap::new));
  }

  /**
   * Checks if the post comment is reacted by the current logged user.
   *
   * @param id the ID of the current logged user
   * @return true if the post comment is reacted by the current logged user, false otherwise
   */
  public boolean isUserPostCommentReactedByCurrentLoggedUserId(Long id) {
    return reactions.stream()
      .anyMatch(reactionDto -> reactionDto.getUserAuthorId()
        .equals(id));
  }

  /**
   * Gets the reaction type of the post comment by the current logged user.
   *
   * @param id the ID of the current logged user
   * @return the reaction type of the post comment by the current logged user, or null if not found
   */
  public ReactionType getUserPostCommentReactionTypeByUserId(Long id) {
    return reactions.stream()
      .filter(reactionDto -> reactionDto.getUserAuthorId()
        .equals(id))
      .map(PostCommentReactionDto::getReactionType)
      .findFirst()
      .orElse(null);
  }

}
