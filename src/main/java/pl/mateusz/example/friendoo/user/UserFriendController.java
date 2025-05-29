package pl.mateusz.example.friendoo.user;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.mateusz.example.friendoo.invitation.InvitationToFriendshipDto;
import pl.mateusz.example.friendoo.invitation.InvitationToFriendshipService;

/**
 * Controller for handling user friend-related operations.
 * Provides endpoints to add friends and manage friendship invitations.
 */
@RestController
@RequestMapping("/api")
public class UserFriendController {

  private final InvitationToFriendshipService invitationToFriendshipService;
  private final UserService userService;

  public UserFriendController(InvitationToFriendshipService invitationToFriendshipService,
                              UserService userService) {
    this.invitationToFriendshipService = invitationToFriendshipService;
    this.userService = userService;
  }

  /**
   * Sends a friend invitation to a user.
   *
   * @param userId the ID of the user to whom the invitation is sent
   * @param authentication the authentication object containing user details
   */
  @PostMapping("/friends")
  public ResponseEntity<InvitationToFriendshipDto> sentFriendInvitation(@RequestParam Long userId,
                                                 Authentication authentication) {
    InvitationToFriendshipDto sentInvitation = invitationToFriendshipService
        .createAndSendInvitation(authentication, userId);
    return ResponseEntity.ok(sentInvitation);
  }

  @DeleteMapping("/friends/{userId}")
  public ResponseEntity<Void> deleteFriendship(@PathVariable Long userId,
                                                 Authentication authentication) {
    userService.deleteFriendship(authentication, userId);
    return ResponseEntity.noContent().build();
  }

  /**
   * Cancels a friendship invitation.
   *
   * @param invitedUserId the ID of the user whose invitation is to be cancelled
   * @param authentication the authentication object containing user details
   * @return the cancelled friendship invitation
   */
  @PutMapping("/friends/{invitedUserId}")
  public ResponseEntity<InvitationToFriendshipDto> cancelFriendshipInvitation(
        @PathVariable Long invitedUserId, Authentication authentication) {
    InvitationToFriendshipDto cancelledFriendshipInvitation = invitationToFriendshipService
        .cancelFriendshipInvitation(authentication, invitedUserId);
    return ResponseEntity.ok(cancelledFriendshipInvitation);
  }

  /**
   * Accepts a friendship invitation.
   *
   * @param senderId the ID of the user who sent the invitation
   * @param authentication the authentication object containing user details
   * @return the accepted friendship invitation
   */
  @PutMapping("/friends/invitations/{senderId}/accept")
  public ResponseEntity<InvitationToFriendshipDto> acceptFriendshipInvitation(
      @PathVariable Long senderId, Authentication authentication) {
    InvitationToFriendshipDto accepted = invitationToFriendshipService
        .acceptFriendshipInvitation(authentication, senderId);
    return ResponseEntity.ok(accepted);
  }

  /**
   * Rejects a friendship invitation.
   *
   * @param senderId the ID of the user who sent the invitation
   * @param authentication the authentication object containing user details
   * @return the rejected friendship invitation
   */
  @PutMapping("/friends/invitations/{senderId}/reject")
  public ResponseEntity<InvitationToFriendshipDto> rejectFriendshipInvitation(
      @PathVariable Long senderId, Authentication authentication) {
    InvitationToFriendshipDto rejected = invitationToFriendshipService
        .rejectFriendshipInvitation(authentication, senderId);
    return ResponseEntity.ok(rejected);
  }

}
