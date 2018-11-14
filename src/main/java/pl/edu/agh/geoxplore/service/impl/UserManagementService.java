package pl.edu.agh.geoxplore.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.edu.agh.geoxplore.entity.ApplicationUser;
import pl.edu.agh.geoxplore.exception.application.UserExistsException;
import pl.edu.agh.geoxplore.exception.application.WrongPasswordException;
import pl.edu.agh.geoxplore.repository.ApplicationUserRepository;
import pl.edu.agh.geoxplore.rest.PasswordChange;
import pl.edu.agh.geoxplore.service.IUserManagementService;

@Service
public class UserManagementService implements IUserManagementService {

    @Autowired
    ApplicationUserRepository applicationUserRepository;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void createUser(ApplicationUser applicationUser) throws UserExistsException {
        if(applicationUserRepository.findByUsername(applicationUser.getUsername()) != null) {
            throw new UserExistsException();
        }
        applicationUser.setPassword(passwordEncoder.encode(applicationUser.getPassword()));
        applicationUser.setExperience(0L);
        applicationUser.setLevel(1L);
        applicationUserRepository.save(applicationUser);
    }

    @Override
    public void changePassword(PasswordChange passwordChange) throws WrongPasswordException {
        ApplicationUser user = authenticationService.getAuthenticatedUser();
        if(!passwordEncoder.matches(passwordChange.getOldPassword(), user.getPassword())) throw new WrongPasswordException();

        user.setPassword(passwordEncoder.encode(passwordChange.getNewPassword()));
        applicationUserRepository.save(user);
    }
}
