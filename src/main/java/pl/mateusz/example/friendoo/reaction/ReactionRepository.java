package pl.mateusz.example.friendoo.reaction;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing `Reaction` entities.
 * Extends `JpaRepository` to provide CRUD operations and database interaction
 * for the `Reaction` entity.
 */
public interface ReactionRepository extends JpaRepository<Reaction, Long> {

  Optional<Reaction> findByReactionType(ReactionType reactionType);

}
