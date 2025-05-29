package pl.mateusz.example.friendoo.invitation;

/**
 * Mapper class for converting InvitationToFriendship entities to InvitationToFriendshipDto.
 */
public class InvitationToFriendshipDtoMapper {

  /**
   * Maps an InvitationToFriendship entity to an InvitationToFriendshipDto.
   *
   * @param invitation the InvitationToFriendship entity to map
   * @return the mapped InvitationToFriendshipDto
   */
  public static InvitationToFriendshipDto mapToDto(InvitationToFriendship invitation) {
    return InvitationToFriendshipDto.builder()
        .id(invitation.getId())
        .senderId(invitation.getSender().getId())
        .receiverId(invitation.getReceiver().getId())
        .requestSentDate(invitation.getRequestSentDate())
        .status(invitation.getStatus())
        .build();
  }
}
