package pl.edu.agh.geoxplore.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.edu.agh.geoxplore.entity.ApplicationUser;

@Service
public class AuthenticationService {
    public ApplicationUser getAuthenticatedUser() {
        return (pl.edu.agh.geoxplore.entity.ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
