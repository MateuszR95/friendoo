package pl.mateusz.example.friendoo.invitation;

import org.springframework.data.jpa.repository.JpaRepository;

@SuppressWarnings("checkstyle:MissingJavadocType")
public interface InvitationToFriendshipRepository
    extends JpaRepository<InvitationToFriendship, Long> {
}
