package pl.edu.agh.geoxplore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.geoxplore.mapper.ChestMapper;
import pl.edu.agh.geoxplore.message.DefaultResponse;
import pl.edu.agh.geoxplore.model.ApplicationUser;
import pl.edu.agh.geoxplore.model.Chest;
import pl.edu.agh.geoxplore.model.HomeLocation;
import pl.edu.agh.geoxplore.model.UserStatistics;
import pl.edu.agh.geoxplore.repository.ChestRepository;
import pl.edu.agh.geoxplore.repository.HomeLocationRepository;
import pl.edu.agh.geoxplore.rest.ChestResponse;
import pl.edu.agh.geoxplore.service.UserStatisticsService;

import java.sql.Date;
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

    @Autowired
    ChestRepository chestRepository;

    @Autowired
    ChestMapper chestMapper;

    @PostMapping("/set-home")
    DefaultResponse home(@RequestBody HomeLocation homeLocation) {
        homeLocation.setDateAdded(new Timestamp(System.currentTimeMillis()));
        homeLocation.setUser(getAuthenticatedUser());
        homeLocationRepository.save(homeLocation);

        return new DefaultResponse("success");
    }

    @GetMapping("/chests")
    List<ChestResponse> getChests() {
        ApplicationUser user = (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Chest> chests = chestRepository.findByUserAndDateCreated(user, new Date(System.currentTimeMillis()));

        if(chests.size() == 0) {
            Chest newChest;
            for (int i = 0; i < 3; i++) {
                newChest = generateChest(user);
                chestRepository.save(newChest);
                chests.add(newChest);
            }
        }

        return chestMapper.ChestToResponse(chests);
    }

    @GetMapping("/my-statistics")
    UserStatistics getMyStatistics() {
        return userStatisticsService.getUserStatistics(getAuthenticatedUser());
    }

    private ApplicationUser getAuthenticatedUser() {
        return (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private Chest generateChest(ApplicationUser user) {
        HomeLocation homeLocation = homeLocationRepository.findFirstByUserOrderByDateAddedDesc(user);

        //todo create better algorithm
        double radius = 0.006;

        double randomLong = homeLocation.getLongitude() + (Math.random()*(radius*2) - radius);
        double randomLat = homeLocation.getLatitude() + (Math.random()*(radius*2) - radius);

        //todo 10 as "magic number"
        return new Chest(-1L, user, randomLong, randomLat, new Date(System.currentTimeMillis()), null, (long) (Math.random()*10));
    }
}
