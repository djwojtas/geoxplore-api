package pl.edu.agh.geoxplore.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import pl.edu.agh.geoxplore.entity.ApplicationUser;

public interface ApplicationUserRepository extends PagingAndSortingRepository<ApplicationUser, Long> {
    ApplicationUser findByUsername(String username);
}
