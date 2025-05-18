package pl.mateusz.example.friendoo.reaction;

/**
 * Mapper class for converting PostCommentReaction to PostCommentReactionDto.
 */
public class PostCommentReactionDtoMapper {

  /**
   * Converts a PostCommentReaction object to a PostCommentReactionDto object.
   *
   * @param postCommentReaction the PostCommentReaction object to convert
   * @return the converted PostCommentReactionDto object
   */
  public static PostCommentReactionDto mapToPostCommentReactionDto(
      PostCommentReaction postCommentReaction) {
    return PostCommentReactionDto.builder()
      .id(postCommentReaction.getId())
      .postCommentId(postCommentReaction.getComment().getId())
      .reactionType(postCommentReaction.getReaction().getReactionType())
      .reactionTime(postCommentReaction.getReactionTime())
      .userAuthorId(postCommentReaction.getAuthor().getId())
      .userAuthorFirstName(postCommentReaction.getAuthor().getFirstName())
      .userAuthorLastName(postCommentReaction.getAuthor().getLastName())
      .pageAuthorId(postCommentReaction.getPageAuthor() != null
        ? postCommentReaction.getPageAuthor().getId()
        : null)
      .build();
  }

}
