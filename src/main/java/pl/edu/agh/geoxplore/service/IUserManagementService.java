package pl.edu.agh.geoxplore.service;

import pl.edu.agh.geoxplore.entity.ApplicationUser;
import pl.edu.agh.geoxplore.exception.application.UserExistsException;
import pl.edu.agh.geoxplore.exception.application.WrongPasswordException;
import pl.edu.agh.geoxplore.rest.PasswordChange;

public interface IUserManagementService {
    void createUser(ApplicationUser applicationUser) throws UserExistsException;

    void changePassword(PasswordChange passwordChange) throws WrongPasswordException;
}
