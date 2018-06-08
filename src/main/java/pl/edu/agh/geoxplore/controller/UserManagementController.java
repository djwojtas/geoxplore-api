package pl.edu.agh.geoxplore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.geoxplore.exception.application.UserExistsException;
import pl.edu.agh.geoxplore.message.DefaultResponse;
import pl.edu.agh.geoxplore.entity.ApplicationUser;
import pl.edu.agh.geoxplore.repository.ApplicationUserRepository;

@RestController
@RequestMapping("/user-management")
public class UserManagementController {

    @Autowired
    ApplicationUserRepository applicationUserRepository;

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

    private ApplicationUser getAuthenticatedUser() {
        return (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
