package pl.mateusz.example.friendoo.reaction;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing PostCommentReaction entities.
 */
public interface PostCommentReactionRepository extends JpaRepository<PostCommentReaction, Long> {

  List<PostCommentReaction> findByCommentIdIn(List<Long> commentIds);

  Optional<PostCommentReaction> findByCommentIdAndAndAuthorId(Long commentId, Long authorId);

}
