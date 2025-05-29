package pl.mateusz.example.friendoo.invitation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.mateusz.example.friendoo.exceptions.FriendshipException;
import pl.mateusz.example.friendoo.exceptions.PendingInvitationToFriendshipAlreadyExists;
import pl.mateusz.example.friendoo.user.User;
import pl.mateusz.example.friendoo.user.UserService;

/**
 * Service class for managing invitations to friendship.
 */
@Service
public class InvitationToFriendshipService {

  private final InvitationToFriendshipRepository invitationToFriendshipRepository;

  private final UserService userService;

  public InvitationToFriendshipService(InvitationToFriendshipRepository
                              invitationToFriendshipRepository, UserService userService) {
    this.invitationToFriendshipRepository = invitationToFriendshipRepository;
    this.userService = userService;
  }

  /**
   * Creates and sends an invitation to friendship.
   *
   * @param authentication the authentication object containing user details
   * @param receiverId     the ID of the user to whom the invitation is sent
   */
  @Transactional
  public InvitationToFriendshipDto createAndSendInvitation(Authentication authentication,
                                                           Long receiverId) {
    User loggedUser = userService.getUserFromAuthentication(authentication);
    User receiverUser = userService.findUserById(receiverId);
    if (loggedUser.getId().equals(receiverUser.getId())) {
      throw new FriendshipException("Nie można wysłać zaproszenia do samego siebie.");
    }
    if (loggedUser.getFriends().contains(receiverUser)) {
      throw new FriendshipException("Użytkownik o id: " + receiverId + " jest już Twoim znajomym.");
    }
    if (isPendingInvitationExistsForUsers(loggedUser.getId(), receiverUser.getId())) {
      throw new PendingInvitationToFriendshipAlreadyExists(
        "Zaproszenie do znajomości już istnieje.");
    }
    InvitationToFriendship invitation = new InvitationToFriendship();
    invitation.setSender(loggedUser);
    invitation.setReceiver(receiverUser);
    invitation.setRequestSentDate(LocalDateTime.now());
    invitation.setStatus(InvitationStatus.PENDING);
    InvitationToFriendship savedInvitation = invitationToFriendshipRepository.save(invitation);
    return InvitationToFriendshipDtoMapper.mapToDto(savedInvitation);
  }

  /**
   * Cancels a friendship invitation.
   *
   * @param authentication the authentication object containing user details
   * @param invitedUserId  the ID of the user to whom the invitation was sent
   * @return the cancelled invitation as a DTO
   */
  @Transactional
  public InvitationToFriendshipDto cancelFriendshipInvitation(Authentication authentication,
                                                              Long invitedUserId) {
    User loggedUser = userService.getUserFromAuthentication(authentication);
    Optional<InvitationToFriendship> invitationToFriendshipOptional =
        invitationToFriendshipRepository.findBySenderIdAndReceiverIdAndStatus(loggedUser.getId(),
        invitedUserId, InvitationStatus.PENDING);
    InvitationToFriendship invitation = invitationToFriendshipOptional.orElseThrow(() ->
      new FriendshipException("Nie znaleziono zaproszenia do znajomości od użytkownika o id: "
        + loggedUser.getId() + " do użytkownika o id: " + invitedUserId));
    invitation.setStatus(InvitationStatus.CANCELED);
    InvitationToFriendship cancelledInvitation = invitationToFriendshipRepository.save(invitation);
    return InvitationToFriendshipDtoMapper.mapToDto(cancelledInvitation);
  }

  /**
   * Accepts a friendship invitation.
   *
   * @param authentication the authentication object containing user details
   * @param invitationSenderId the ID of the user who sent the invitation
   * @return the accepted invitation as a DTO
   */
  @Transactional
  public InvitationToFriendshipDto acceptFriendshipInvitation(Authentication authentication,
                                                              Long invitationSenderId) {
    User loggedUser = userService.getUserFromAuthentication(authentication);
    User invitationSender = userService.findUserById(invitationSenderId);
    InvitationToFriendship invitation = invitationToFriendshipRepository
        .findBySenderIdAndReceiverIdAndStatus(invitationSenderId, loggedUser.getId(),
          InvitationStatus.PENDING)
        .orElseThrow(() -> new FriendshipException("Nie znaleziono zaproszenia do"
          + " znajomości od użytkownika o id: " + invitationSenderId + " do użytkownika o id: "
          + loggedUser.getId()));
    invitation.setStatus(InvitationStatus.ACCEPTED);
    invitation.setAcceptedDate(LocalDateTime.now());
    loggedUser.getFriends().add(invitationSender);
    invitationSender.getFriends().add(loggedUser);
    userService.saveUser(loggedUser);
    userService.saveUser(invitationSender);
    InvitationToFriendship acceptedInvitation = invitationToFriendshipRepository.save(invitation);
    return InvitationToFriendshipDtoMapper.mapToDto(acceptedInvitation);
  }

  /**
   * Rejects a friendship invitation.
   *
   * @param authentication the authentication object containing user details
   * @param invitationSenderId the ID of the user who sent the invitation
   * @return the rejected invitation as a DTO
   */
  @Transactional
  public InvitationToFriendshipDto rejectFriendshipInvitation(Authentication authentication,
                                                              Long invitationSenderId) {
    User loggedUser = userService.getUserFromAuthentication(authentication);
    InvitationToFriendship invitation = invitationToFriendshipRepository
        .findBySenderIdAndReceiverIdAndStatus(invitationSenderId, loggedUser.getId(),
        InvitationStatus.PENDING)
        .orElseThrow(() -> new FriendshipException("Nie znaleziono zaproszenia do"
        + " znajomości od użytkownika o id: " + invitationSenderId + " do użytkownika o id: "
        + loggedUser.getId()));
    invitation.setStatus(InvitationStatus.REJECTED);
    invitation.setDeclinedDate(LocalDateTime.now());
    InvitationToFriendship rejectedInvitation = invitationToFriendshipRepository.save(invitation);
    return InvitationToFriendshipDtoMapper.mapToDto(rejectedInvitation);
  }

  private boolean isPendingInvitationExistsForUsers(Long userId1, Long userId2) {
    return invitationToFriendshipRepository.existsBySenderIdAndReceiverIdAndStatusIs(userId1,
      userId2, InvitationStatus.PENDING)
        || invitationToFriendshipRepository.existsBySenderIdAndReceiverIdAndStatusIs(userId2,
      userId1, InvitationStatus.PENDING);
  }


  /**
   * Retrieves the IDs of users who have sent pending friend invitations to the logged-in user.
   *
   * @param loggedUserEmail the email of the logged-in user
   * @return a list of user IDs who have sent pending friend invitations
   */
  public List<Long> getIdsOfUsersWithSentPendingFriendInvitations(String loggedUserEmail) {
    return invitationToFriendshipRepository
      .findAllBySenderEmailAndStatus(loggedUserEmail, InvitationStatus.PENDING)
      .stream()
      .map(invitation -> invitation.getReceiver().getId())
      .toList();
  }

  /**
   * Retrieves the IDs of users who have sent pending friend invitations to the logged-in user.
   *
   * @param loggedUserEmail the email of the logged-in user
   * @return a list of user IDs who have sent pending friend invitations
   */
  public List<Long> getSenderIdsOfPendingFriendInvitationsReceivedByUser(String loggedUserEmail) {
    return invitationToFriendshipRepository
      .findAllByReceiverEmailAndStatusIs(loggedUserEmail, InvitationStatus.PENDING)
      .stream()
      .map(invitation -> invitation.getSender().getId())
      .toList();
  }

}
