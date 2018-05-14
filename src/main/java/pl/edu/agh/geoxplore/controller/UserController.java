package pl.edu.agh.geoxplore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.geoxplore.message.DefaultResponse;
import pl.edu.agh.geoxplore.model.ApplicationUser;
import pl.edu.agh.geoxplore.model.Chest;
import pl.edu.agh.geoxplore.model.HomeLocation;
import pl.edu.agh.geoxplore.model.UserStatistics;
import pl.edu.agh.geoxplore.repository.HomeLocationRepository;
import pl.edu.agh.geoxplore.service.UserStatisticsService;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    HomeLocationRepository homeLocationRepository;

    @Autowired
    UserStatisticsService userStatisticsService;

    @PostMapping("/set-home")
    DefaultResponse home(@RequestBody HomeLocation homeLocation) {
        homeLocation.setDate_added(new Timestamp(System.currentTimeMillis()));
        homeLocation.setUser(getAuthenticatedUser());
        homeLocationRepository.save(homeLocation);

        return new DefaultResponse("success");
    }

    @GetMapping("/chests")
    List<Chest> getChests() {
        //ApplicationUser user = (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Chest> chests = new ArrayList<>();
        chests.add(new Chest(19.921969, 50.066245, true));
        chests.add(new Chest(19.901499, 50.068715, false));
        chests.add(new Chest(19.919362, 50.065950, false));

        return chests;
    }

    @GetMapping("/my-statistics")
    UserStatistics getMyStatistics() {
        return userStatisticsService.getUserStatistics(getAuthenticatedUser());
    }

    private ApplicationUser getAuthenticatedUser() {
        return (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
