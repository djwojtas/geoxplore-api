package pl.edu.agh.geoxplore.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import pl.edu.agh.geoxplore.entity.ApplicationUser;

import java.util.List;

public interface ApplicationUserRepository extends PagingAndSortingRepository<ApplicationUser, Long> {
    ApplicationUser findByUsername(String username);
    List<ApplicationUser> findAllByUsernameContainingIgnoreCase(String username);
}
