package pl.edu.agh.geoxplore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.geoxplore.exception.application.UserExistsException;
import pl.edu.agh.geoxplore.message.DefaultResponse;
import pl.edu.agh.geoxplore.model.ApplicationUser;
import pl.edu.agh.geoxplore.model.HomeLocation;
import pl.edu.agh.geoxplore.repository.ApplicationUserRepository;
import pl.edu.agh.geoxplore.repository.HomeLocationRepository;
import pl.edu.agh.geoxplore.security.UserPrincipal;

import java.security.Principal;
import java.sql.Timestamp;

@RestController
@RequestMapping("/user-management")
public class UserManagementController {
    @Autowired
    ApplicationUserRepository applicationUserRepository;

    @Autowired
    HomeLocationRepository homeLocationRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/create-user")
    DefaultResponse add(@RequestBody ApplicationUser applicationUser) throws UserExistsException {
        if(applicationUserRepository.findByUsername(applicationUser.getUsername()) != null) {
            throw new UserExistsException();
        }
        applicationUser.setPassword(passwordEncoder.encode(applicationUser.getPassword()));
        applicationUserRepository.save(applicationUser);

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
        }
        return ret;
    }

    @PostMapping("/set-home")
    DefaultResponse home(@RequestBody HomeLocation homeLocation) {
        ApplicationUser user = (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        homeLocation.setDate_added(new Timestamp(System.currentTimeMillis()));
        homeLocation.setUser(user);
        homeLocationRepository.save(homeLocation);

        return new DefaultResponse("success");
    }
}
