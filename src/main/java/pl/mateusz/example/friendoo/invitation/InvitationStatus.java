package pl.mateusz.example.friendoo.invitation;

@SuppressWarnings("checkstyle:MissingJavadocType")
public enum InvitationStatus {

  PENDING("oczekujące"),
  ACCEPTED("zaakceptowane"),
  DECLINED("odrzucone");

  private String description;

  InvitationStatus(String description) {
    this.description = description;
  }
}
