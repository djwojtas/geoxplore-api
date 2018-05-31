package pl.edu.agh.geoxplore.repository;

import org.springframework.data.repository.CrudRepository;
import pl.edu.agh.geoxplore.entity.ApplicationUser;
import pl.edu.agh.geoxplore.entity.Chest;

import java.sql.Date;
import java.util.List;

public interface ChestRepository extends CrudRepository<Chest, Long> {
    List<Chest> findByUserAndDateCreated(ApplicationUser emailAddress, Date date);
    Chest findFirstByUserAndId(ApplicationUser user, Long id);
}
