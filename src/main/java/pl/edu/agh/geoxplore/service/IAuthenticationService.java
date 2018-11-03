package pl.edu.agh.geoxplore.service;

import pl.edu.agh.geoxplore.entity.ApplicationUser;

public interface IAuthenticationService {
    ApplicationUser getAuthenticatedUser();
}
