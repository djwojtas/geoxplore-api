package pl.edu.agh.geoxplore.service;

import pl.edu.agh.geoxplore.entity.ApplicationUser;
import pl.edu.agh.geoxplore.rest.ChestResponse;
import pl.edu.agh.geoxplore.rest.OpenedChest;

import java.util.List;

public interface IChestService {
    OpenedChest openChest(ApplicationUser applicationUser, Long id);

    List<ChestResponse> getUserChests(ApplicationUser applicationUser);
}
