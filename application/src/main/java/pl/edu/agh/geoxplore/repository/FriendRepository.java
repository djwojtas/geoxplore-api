package pl.edu.agh.geoxplore.repository;

import org.springframework.data.repository.CrudRepository;
import pl.edu.agh.geoxplore.model.Friend;

public interface FriendRepository extends CrudRepository<Friend, Long> {
}
