package pl.mateusz.example.friendoo.reaction;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository interface for managing `PostReaction` entities.
 * Extends `JpaRepository` to provide CRUD operations and database interaction
 * for the `PostReaction` entity.
 */
public interface PostReactionRepository extends JpaRepository<PostReaction, Long> {
  @Query("""
  SELECT pr FROM PostReaction pr
  LEFT JOIN FETCH pr.author
  LEFT JOIN FETCH pr.userPost
  LEFT JOIN FETCH pr.pageAuthor
  LEFT JOIN FETCH pr.pagePost
  LEFT JOIN FETCH pr.reaction
  WHERE pr.userPost.id IN :postIds
        """)
  List<PostReaction> findByUserPostIds(@Param("postIds") List<Long> postIds);

  Optional<PostReaction> findByUserPostIdAndAuthorId(Long postId, Long authorId);

}
