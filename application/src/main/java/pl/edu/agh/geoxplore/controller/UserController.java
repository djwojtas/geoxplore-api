package pl.edu.agh.geoxplore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.geoxplore.exception.application.HomeLocationNotSetException;
import pl.edu.agh.geoxplore.mapper.ChestMapper;
import pl.edu.agh.geoxplore.message.DefaultResponse;
import pl.edu.agh.geoxplore.model.ApplicationUser;
import pl.edu.agh.geoxplore.model.Chest;
import pl.edu.agh.geoxplore.model.HomeLocation;
import pl.edu.agh.geoxplore.repository.ApplicationUserRepository;
import pl.edu.agh.geoxplore.repository.ChestRepository;
import pl.edu.agh.geoxplore.repository.HomeLocationRepository;
import pl.edu.agh.geoxplore.rest.ChestResponse;
import pl.edu.agh.geoxplore.rest.OpenedChest;
import pl.edu.agh.geoxplore.rest.UserStatistics;
import pl.edu.agh.geoxplore.service.UserStatisticsService;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

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
    ApplicationUserRepository applicationUserRepository;

    @Autowired
    ChestMapper chestMapper;

    //todo i really REALLY need to move logic to service layer
    private ApplicationUser getAuthenticatedUser() {
        return (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

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
            }
            //fixme remove mock for fronts
            HomeLocation homeLocation = homeLocationRepository.findFirstByUserOrderByDateAddedDesc(user);
            Chest c = new Chest(-1L, user, homeLocation.getLongitude() - 0.00024, homeLocation.getLatitude() - 0.00024, new Date(System.currentTimeMillis()), null, (long) (Math.random()*10));
            chestRepository.save(c);
        }

        //todo change to local variable now go to sleep
        chests = chestRepository.findByUserAndDateCreated(user, new Date(System.currentTimeMillis()));
        return chestMapper.ChestToResponse(chests);
    }

    @GetMapping("/my-statistics")
    UserStatistics getMyStatistics() {
        return userStatisticsService.getUserStatistics(getAuthenticatedUser());
    }

    private Chest generateChest(ApplicationUser user) {
        HomeLocation homeLocation = homeLocationRepository.findFirstByUserOrderByDateAddedDesc(user);

        //todo create better algorithm
        double radius = 0.006;


        double randomLong = homeLocation.getLongitude() + (Math.random()*(radius*2) - radius);
        double randomLat = homeLocation.getLatitude() + (Math.random()*(radius*2) - radius);

        //todo 10 as "magic number"
        return new Chest(-1L, user, randomLong, randomLat, new Date(System.currentTimeMillis()), null, getChestLevel());
    }

    public long calculateExpFromChest(Chest chest) {
        return chest.getValue() * 10;
    }

    private long getChestLevel() {
        double roll = Math.random();
        //todo fix magic numbers to non-mock
        //0.6 0.9 0.975
        if(roll < 0.25) {
            return 1;
        } else if(roll < 0.5) {
            return 2;
        } else if(roll < 0.75) {
            return 3;
        } else {
            return 4;
        }
    }

    @PostMapping("/open-chest/{id}")
    private OpenedChest openChest(@PathVariable(name = "id") Long id) {
        Optional<Chest> chest = chestRepository.findById(id);
        chest.get().setDateFound(new Timestamp(System.currentTimeMillis()));
        chestRepository.save(chest.get()); //todo make sure that chest can only by opened once

        ApplicationUser user = getAuthenticatedUser();

        //todo move generating exp somewhere else?
        Long gainedExp = calculateExpFromChest(chest.get());
        user.setExperience(user.getExperience() + gainedExp);
        user.setLevel(userStatisticsService.getLevelFromExp(user.getExperience()));

        applicationUserRepository.save(user);

        OpenedChest response = new OpenedChest();
        response.setExpGained(gainedExp);

        return response;
    }

    @GetMapping("/get-home")
    String getHomeLocation() throws HomeLocationNotSetException {
        ApplicationUser user = (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<HomeLocation> homeLocation = Optional.ofNullable(homeLocationRepository.findFirstByUserOrderByDateAddedDesc(user));

        if(!homeLocation.isPresent()) throw new HomeLocationNotSetException();

        //todo god why
        return "{\n\"longitude\":\"" + homeLocation.get().getLongitude() + "\",\n\"latitude\":\"" + homeLocation.get().getLatitude() + "\"\n}";
    }
}


