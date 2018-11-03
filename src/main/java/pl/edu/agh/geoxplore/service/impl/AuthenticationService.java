package pl.edu.agh.geoxplore.service.impl;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.edu.agh.geoxplore.entity.ApplicationUser;
import pl.edu.agh.geoxplore.service.IAuthenticationService;

@Service
public class AuthenticationService implements IAuthenticationService {
    @Override
    public ApplicationUser getAuthenticatedUser() {
        return (pl.edu.agh.geoxplore.entity.ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
