package pl.edu.agh.geoxplore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.geoxplore.dto.TestModel;
import pl.edu.agh.geoxplore.repository.TestRepository;

@RestController
public class TestController {
    @Autowired
    TestRepository testRepository;

    private Long id = 1L;

    @GetMapping("/add")
    String add() {
        TestModel t = new TestModel(1L, "ASD", "WTF", "LOL");
        testRepository.save(t);
        return "success";
    }

    @GetMapping("/read")
    String read() {
        String ret = "";
        for(TestModel t : testRepository.findAll()) {
            ret += t.toString();
        }
        return ret;
    }
}
