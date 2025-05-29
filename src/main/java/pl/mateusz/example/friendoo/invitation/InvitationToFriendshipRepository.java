package pl.mateusz.example.friendoo.invitation;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for the InvitationToFriendship entity.
 */
public interface InvitationToFriendshipRepository
    extends JpaRepository<InvitationToFriendship, Long> {

  boolean existsBySenderIdAndReceiverIdAndStatusIs(Long senderId,
                                      Long receiverId, InvitationStatus status);

  List<InvitationToFriendship> findAllBySenderEmailAndStatus(String senderEmail,
                                                             InvitationStatus status);

  List<InvitationToFriendship> findAllByReceiverEmailAndStatusIs(String receiverEmail,
                                                             InvitationStatus status);

  Optional<InvitationToFriendship> findBySenderIdAndReceiverIdAndStatus(Long senderId,
                                      Long receiverId, InvitationStatus invitationStatus);


}
