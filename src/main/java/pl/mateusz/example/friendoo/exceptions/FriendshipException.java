package pl.mateusz.example.friendoo.exceptions;

/**
 * Custom exception class for handling friendship-related errors.
 * This exception is thrown when there is an issue with friendship operations,
 * such as sending invitations or managing friendships.
 */
public class FriendshipException extends RuntimeException {

  public FriendshipException(String message) {
    super(message);
  }

}
