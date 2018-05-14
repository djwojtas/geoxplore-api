package pl.edu.agh.geoxplore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.geoxplore.model.ApplicationUser;
import pl.edu.agh.geoxplore.model.RankingUser;
import pl.edu.agh.geoxplore.repository.ApplicationUserRepository;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/community")
public class CommunityController {

    @Autowired
    ApplicationUserRepository applicationUserRepository;

    @GetMapping("/ranking")
    List<RankingUser> getUserRanking() {
        List<RankingUser> ranking = new ArrayList<>();

        for(ApplicationUser applicationUser : applicationUserRepository.findAll()) {
            ranking.add(new RankingUser(
                    applicationUser.getUsername(),
                    applicationUser.getLevel(),
                    (long) (Math.random()*50)
            ));
        }

        return ranking;
    }
}
