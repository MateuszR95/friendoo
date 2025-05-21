package pl.mateusz.example.friendoo.post.postversion;

/**
 * Mapper class to convert PostVersion entity to PostVersionDto.
 */
public class PostVersionDtoMapper {

  /**
   * Converts a PostVersion entity to a PostVersionDto.
   *
   * @param postVersion The PostVersion entity to convert.
   * @return The converted PostVersionDto.
   */
  public static PostVersionDto mapToDto(PostVersion postVersion) {
    return PostVersionDto.builder()
        .id(postVersion.getId())
        .authorFirstName(postVersion.getUserPost().getAuthor().getFirstName())
        .authorLastName(postVersion.getUserPost().getAuthor().getLastName())
        .editorId(postVersion.getEditorUserId())
        .content(postVersion.getContent())
        .createdAt(postVersion.getCreatedAt())
        .build();
  }
}
