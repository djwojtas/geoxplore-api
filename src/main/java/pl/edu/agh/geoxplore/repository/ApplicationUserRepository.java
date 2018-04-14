package pl.edu.agh.geoxplore.repository;

import org.springframework.data.repository.CrudRepository;
import pl.edu.agh.geoxplore.model.ApplicationUser;

public interface ApplicationUserRepository extends CrudRepository<ApplicationUser, Long> {
    ApplicationUser findByUsername(String username);
}
