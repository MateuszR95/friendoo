package pl.mateusz.example.friendoo.comment.user;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 Repository interface for managing PostComment entities.
 Extends JpaRepository to provide CRUD operations and database interaction
 for the PostComment entity. */
public interface PostCommentRepository extends JpaRepository<PostComment, Long> {

}
