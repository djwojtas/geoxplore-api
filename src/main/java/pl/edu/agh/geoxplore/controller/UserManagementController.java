package pl.edu.agh.geoxplore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.geoxplore.exception.application.UserExistsException;
import pl.edu.agh.geoxplore.message.DefaultResponse;
import pl.edu.agh.geoxplore.model.*;
import pl.edu.agh.geoxplore.repository.ApplicationUserRepository;
import pl.edu.agh.geoxplore.repository.HomeLocationRepository;
import pl.edu.agh.geoxplore.service.UserStatisticsService;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user-management")
public class UserManagementController {
    @Autowired
    ApplicationUserRepository applicationUserRepository;

    @Autowired
    HomeLocationRepository homeLocationRepository;

    @Autowired
    UserStatisticsService userStatisticsService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/create-user")
    DefaultResponse add(@RequestBody ApplicationUser applicationUser) throws UserExistsException {
        if(applicationUserRepository.findByUsername(applicationUser.getUsername()) != null) {
            throw new UserExistsException();
        }
        applicationUser.setPassword(passwordEncoder.encode(applicationUser.getPassword()));
        long exp = (long) (Math.random()*150);
        applicationUser.setExperience(exp); //TODO remove mock
        if(exp < 67) {
            applicationUser.setLevel(1L);
        } else {
            applicationUser.setLevel(2L);
        }

        applicationUserRepository.save(applicationUser);

        return new DefaultResponse("success");
    }

    @GetMapping("/reroll")
    DefaultResponse add() throws UserExistsException {
        ApplicationUser currentUser = getAuthenticatedUser();

        long exp = (long) (Math.random()*150);
        currentUser.setExperience(exp); //TODO remove mock
        if(exp < 67) {
            currentUser.setLevel(1L);
        } else {
            currentUser.setLevel(2L);
        }

        applicationUserRepository.save(currentUser);

        return new DefaultResponse("success");
    }

    //TODO delete this test
    @GetMapping("/list-users")
    String read() {
        String ret = "";
        for(ApplicationUser t : applicationUserRepository.findAll()) {
            ret += "" +
                    "nick: " + t.getUsername() +
                    "\nhomeLocations: ";
            for (HomeLocation l : t.getHome_locations()) {
                ret += "\nlong: " + l.getLongitude() +
                        " lat: " + l.getLatitude();
            }
            ret += "\n\n";
        }
        return ret;
    }

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
