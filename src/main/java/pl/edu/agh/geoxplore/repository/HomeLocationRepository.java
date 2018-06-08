package pl.edu.agh.geoxplore.repository;

import org.springframework.data.repository.CrudRepository;
import pl.edu.agh.geoxplore.entity.ApplicationUser;
import pl.edu.agh.geoxplore.entity.HomeLocation;

public interface HomeLocationRepository extends CrudRepository<HomeLocation, Long> {
    HomeLocation findFirstByUserOrderByDateAddedDesc(ApplicationUser user);
}
