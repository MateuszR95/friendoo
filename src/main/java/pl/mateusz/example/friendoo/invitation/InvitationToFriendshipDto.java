package pl.mateusz.example.friendoo.invitation;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object representing an invitation to friendship.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvitationToFriendshipDto {

  private Long id;
  private Long senderId;
  private Long receiverId;
  private LocalDateTime requestSentDate;
  private InvitationStatus status;
}
