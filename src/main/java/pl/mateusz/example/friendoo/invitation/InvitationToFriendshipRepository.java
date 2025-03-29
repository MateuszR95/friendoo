package pl.mateusz.example.friendoo.invitation;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for the InvitationToFriendship entity.
 */
public interface InvitationToFriendshipRepository
    extends JpaRepository<InvitationToFriendship, Long> {
}
