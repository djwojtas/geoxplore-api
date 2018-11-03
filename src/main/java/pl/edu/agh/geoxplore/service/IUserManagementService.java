package pl.edu.agh.geoxplore.service;

import pl.edu.agh.geoxplore.entity.ApplicationUser;
import pl.edu.agh.geoxplore.exception.application.UserExistsException;

public interface IUserManagementService {
    void createUser(ApplicationUser applicationUser) throws UserExistsException;
}
