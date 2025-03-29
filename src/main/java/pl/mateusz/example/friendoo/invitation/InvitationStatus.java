package pl.mateusz.example.friendoo.invitation;

/**
 * Enum representing the status of an invitation.
 */
public enum InvitationStatus {

  PENDING("oczekujące"),
  ACCEPTED("zaakceptowane"),
  DECLINED("odrzucone");

  private final String description;

  InvitationStatus(String description) {
    this.description = description;
  }
}
