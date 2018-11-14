package pl.edu.agh.geoxplore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.geoxplore.entity.ApplicationUser;
import pl.edu.agh.geoxplore.exception.application.UserExistsException;
import pl.edu.agh.geoxplore.exception.application.WrongPasswordException;
import pl.edu.agh.geoxplore.message.DefaultResponse;
import pl.edu.agh.geoxplore.rest.PasswordChange;
import pl.edu.agh.geoxplore.service.IUserManagementService;

@RestController
@RequestMapping("/user-management")
public class UserManagementController {

    @Autowired
    IUserManagementService userManagementService;

    @PostMapping("/password/change")
    public DefaultResponse avatarUpload(@RequestBody PasswordChange passwordChange) throws WrongPasswordException {
        userManagementService.changePassword(passwordChange);
        return new DefaultResponse("success");
    }

    @PostMapping("/user/create")
    public DefaultResponse add(@RequestBody ApplicationUser applicationUser) throws UserExistsException {
        userManagementService.createUser(applicationUser);
        return new DefaultResponse("success");
    }
}
