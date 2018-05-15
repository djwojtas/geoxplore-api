package pl.edu.agh.geoxplore.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import pl.edu.agh.geoxplore.model.ApplicationUser;
import pl.edu.agh.geoxplore.model.Chest;

import java.sql.Date;
import java.util.List;

public interface ChestRepository extends CrudRepository<Chest, Long> {
    List<Chest> findByUserAndDateCreated(ApplicationUser emailAddress, Date date);
    Chest findFirstByUserAndId(ApplicationUser user, Long id);
}
