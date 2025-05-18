package pl.mateusz.example.friendoo.comment;

import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 Repository interface for managing PostComment entities.
 Extends JpaRepository to provide CRUD operations and database interaction
 for the PostComment entity. */
public interface PostCommentRepository extends JpaRepository<PostComment, Long> {

  List<PostComment> findAllByUserPostId(Long id);

  List<PostComment> findAllByPagePostId(Long id);

  @Query("""
  SELECT DISTINCT pc FROM PostComment pc
  LEFT JOIN FETCH pc.author
  LEFT JOIN FETCH pc.pageAuthor
  LEFT JOIN FETCH pc.userPost
  LEFT JOIN FETCH pc.pagePost
  LEFT JOIN FETCH pc.reactions
  WHERE pc.userPost.id IN :postIds
        """)
  List<PostComment> findByUserPostIds(@Param("postIds") List<Long> postIds);
}
