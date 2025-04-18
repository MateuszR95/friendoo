package pl.mateusz.example.friendoo.comment.user;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for UserPostComment entity.
 */
public interface UserPostCommentRepository extends JpaRepository<UserPostComment, Long> {

}
