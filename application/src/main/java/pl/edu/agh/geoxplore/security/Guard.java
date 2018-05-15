package pl.edu.agh.geoxplore.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import pl.edu.agh.geoxplore.model.ApplicationUser;
import pl.edu.agh.geoxplore.model.Chest;
import pl.edu.agh.geoxplore.repository.ApplicationUserRepository;
import pl.edu.agh.geoxplore.repository.ChestRepository;

@Component
public class Guard {

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Autowired
    private ChestRepository chestRepository;

    public boolean checkUserChestId(Authentication authentication, int id) {
        Chest chest = chestRepository.findFirstByUserAndId((ApplicationUser) authentication.getPrincipal(), (long) id);
        return chest != null;
    }
}