package pl.mateusz.example.friendoo.reaction.user;

/**
 * Mapper class for converting `UserPostReaction` entities to `UserPostReactionDto` objects.
 * This class provides a utility method to map the data from the `UserPostReaction` entity
 * to the `UserPostReactionDto`, which is used for data transfer purposes.
 */
public class UserPostReactionDtoMapper {

  /**
   * Maps a `UserPostReaction` entity to a `UserPostReactionDto`.
   *
   * @param userPostReaction the `UserPostReaction` entity to map
   * @return the mapped `UserPostReactionDto`
   */
  public static UserPostReactionDto mapToDto(UserPostReaction userPostReaction) {
    return UserPostReactionDto.builder()
        .id(userPostReaction.getId())
        .authorId(userPostReaction.getUser().getId())
        .userPostId(userPostReaction.getPost().getId())
        .authorFirstName(userPostReaction.getUser().getFirstName())
        .authorLastName(userPostReaction.getUser().getLastName())
        .reactionType(userPostReaction.getReaction().getReactionType())
        .reactionTime(userPostReaction.getReactionTime())
        .build();
  }
}
