package pl.mateusz.example.friendoo.exceptions;

/**
 * Exception thrown when a pending invitation to friendship already exists.
 * This exception is used to indicate that an attempt was made to create
 * a friendship invitation that is already pending between two users.
 */
public class PendingInvitationToFriendshipAlreadyExists extends RuntimeException {

  public PendingInvitationToFriendshipAlreadyExists(String message) {
    super(message);
  }

}
