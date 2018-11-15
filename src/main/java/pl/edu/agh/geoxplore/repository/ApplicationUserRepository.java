package pl.edu.agh.geoxplore.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import pl.edu.agh.geoxplore.entity.ApplicationUser;

import java.util.List;

public interface ApplicationUserRepository extends PagingAndSortingRepository<ApplicationUser, Long> {
    ApplicationUser findByUsername(String username);
    List<ApplicationUser> findAllByUsernameContainingIgnoreCase(String username);
    @Query(nativeQuery = true,
            value = "SELECT * FROM users u WHERE u.id in (SELECT friend_id FROM friends WHERE user_id = ?1)",
            countQuery = "SELECT COUNT(*) FROM users u WHERE u.id in (SELECT friend_id FROM friends WHERE user_id = ?1)"
    )
    List<ApplicationUser> findAllFriends(Long userId, Pageable pageable);
}
