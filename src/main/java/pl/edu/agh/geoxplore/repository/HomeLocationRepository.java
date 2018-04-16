package pl.edu.agh.geoxplore.repository;

import org.springframework.data.repository.CrudRepository;
import pl.edu.agh.geoxplore.model.HomeLocation;

public interface HomeLocationRepository extends CrudRepository<HomeLocation, Long> {
}
