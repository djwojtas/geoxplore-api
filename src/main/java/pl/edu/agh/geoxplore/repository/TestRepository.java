package pl.edu.agh.geoxplore.repository;

import org.springframework.data.repository.CrudRepository;
import pl.edu.agh.geoxplore.dto.TestModel;

public interface TestRepository extends CrudRepository<TestModel, Long> {}
