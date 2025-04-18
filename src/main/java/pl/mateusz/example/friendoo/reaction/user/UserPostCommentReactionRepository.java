package pl.mateusz.example.friendoo.reaction.user;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 Repository interface for managing UserPostCommentReaction entities.
 Extends JpaRepository to provide CRUD operations and database interaction
 for the UserPostCommentReaction entity. */
public interface UserPostCommentReactionRepository extends JpaRepository<
      UserPostCommentReaction, Long> {

  List<UserPostCommentReaction> findAllByComment_Id(Long userPostCommentId);

}
