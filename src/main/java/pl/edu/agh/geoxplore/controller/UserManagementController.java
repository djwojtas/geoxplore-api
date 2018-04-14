package pl.edu.agh.geoxplore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.geoxplore.exception.application.UserExistsException;
import pl.edu.agh.geoxplore.model.ApplicationUser;
import pl.edu.agh.geoxplore.repository.ApplicationUserRepository;

@RestController
@RequestMapping("/user-management")
public class UserManagementController {
    @Autowired
    ApplicationUserRepository applicationUserRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/create-user")
    String add(@RequestBody ApplicationUser applicationUser) throws UserExistsException {
        if(applicationUserRepository.findByUsername(applicationUser.getUsername()) != null) {
            throw new UserExistsException();
        }
        applicationUser.setPassword(passwordEncoder.encode(applicationUser.getPassword()));
        applicationUserRepository.save(applicationUser);
        return "success";
    }

    //TODO delete this test
    @GetMapping("/list-users")
    String read() {
        String ret = "";
        for(ApplicationUser t : applicationUserRepository.findAll()) {
            ret += t.toString();
        }
        return ret;
    }
}
