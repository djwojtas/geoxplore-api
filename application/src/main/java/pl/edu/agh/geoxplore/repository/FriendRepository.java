package pl.edu.agh.geoxplore.repository;

import org.springframework.data.repository.CrudRepository;
import pl.edu.agh.geoxplore.entity.Friend;

public interface FriendRepository extends CrudRepository<Friend, Long> {
}
