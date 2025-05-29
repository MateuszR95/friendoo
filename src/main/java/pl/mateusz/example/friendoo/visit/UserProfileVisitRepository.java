package pl.mateusz.example.friendoo.visit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for user profile visit.
 */
@Repository
public interface UserProfileVisitRepository extends JpaRepository<UserProfileVisit, Long> {

}
