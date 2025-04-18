package pl.mateusz.example.friendoo.reaction.user;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.mateusz.example.friendoo.reaction.Reaction;

/**
 * Repository interface for managing `UserPostReaction` entities.
 * Extends `JpaRepository` to provide CRUD operations and database interaction
 * for the `UserPostReaction` entity.
 */
public interface UserPostReactionRepository extends JpaRepository<UserPostReaction, Long> {

  List<UserPostReaction> findByPostIdOrderByReactionTimeDesc(Long postId);

  Optional<UserPostReaction> findByReaction(Reaction reaction);

  Optional<UserPostReaction> findByPostIdAndUserId(Long postId, Long userId);


}
