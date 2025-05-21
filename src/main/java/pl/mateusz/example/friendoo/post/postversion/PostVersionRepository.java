package pl.mateusz.example.friendoo.post.postversion;

import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing PostVersion entities.
 */
@Repository
public interface PostVersionRepository extends JpaRepository<PostVersion, Long> {

  List<PostVersion> findAllByUserPostId(Long userPostId);

  @Query("SELECT DISTINCT p.userPost.id FROM PostVersion p WHERE p.userPost IS NOT NULL")
  Set<Long> findDistinctUserPostIdsWithVersions();
}
