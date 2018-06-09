package pl.edu.agh.geoxplore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.geoxplore.exception.application.UserExistsException;
import pl.edu.agh.geoxplore.message.DefaultResponse;
import pl.edu.agh.geoxplore.entity.ApplicationUser;
import pl.edu.agh.geoxplore.repository.ApplicationUserRepository;
import pl.edu.agh.geoxplore.service.UserManagementService;

@RestController
@RequestMapping("/user-management")
public class UserManagementController {

    @Autowired
    UserManagementService userManagementService;

    @PostMapping("/create-user")
    DefaultResponse add(@RequestBody ApplicationUser applicationUser) throws UserExistsException {
        userManagementService.createUser(applicationUser);
        return new DefaultResponse("success");
    }
}
