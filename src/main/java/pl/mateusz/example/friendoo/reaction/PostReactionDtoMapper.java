package pl.mateusz.example.friendoo.reaction;

/**
 Utility class for mapping PostReaction entities to PostReactionDto objects.
 This class provides a method to convert the entity into its corresponding DTO
 for data transfer purposes. */
public class PostReactionDtoMapper {

  /**
   * Maps a `PostReaction` entity to a `PostReactionDto`.
   *
   * @param postReaction the `PostReaction` entity to map
   * @return the mapped `PostReactionDto`
   */
  public static PostReactionDto mapToDto(PostReaction postReaction) {
    return PostReactionDto.builder()
      .id(postReaction.getId())
      .userAuthorId(
        postReaction.getAuthor() != null ? postReaction.getAuthor().getId() : null)
      .pageAuthorId(
        postReaction.getPageAuthor() != null ? postReaction.getPageAuthor().getId() : null)
      .pagePostId(
        postReaction.getPagePost() != null ? postReaction.getPagePost().getId() : null)
      .userPostId(
        postReaction.getUserPost() != null ? postReaction.getUserPost().getId() : null)
      .authorFirstName(
        postReaction.getAuthor() != null ? postReaction.getAuthor().getFirstName() : null)
      .authorLastName(
        postReaction.getAuthor() != null ? postReaction.getAuthor().getLastName() : null)
      .pageName(
        postReaction.getPageAuthor() != null ? postReaction.getPageAuthor().getName() : null)
      .reactionType(
        postReaction.getReaction() != null ? postReaction.getReaction().getReactionType() : null)
      .reactionTime(postReaction.getReactionTime())
      .build();
  }

}
