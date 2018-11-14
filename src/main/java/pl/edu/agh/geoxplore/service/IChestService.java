package pl.edu.agh.geoxplore.service;

import pl.edu.agh.geoxplore.entity.ApplicationUser;
import pl.edu.agh.geoxplore.exception.application.ChestAlreadyOpenedException;
import pl.edu.agh.geoxplore.exception.application.ChestDoesNotExistException;
import pl.edu.agh.geoxplore.exception.application.HomeLocationNotSetException;
import pl.edu.agh.geoxplore.rest.ChestResponse;
import pl.edu.agh.geoxplore.rest.OpenedChest;

import java.util.List;

public interface IChestService {
    OpenedChest openChest(ApplicationUser applicationUser, Long id) throws ChestDoesNotExistException, ChestAlreadyOpenedException;

    List<ChestResponse> getUserChests(ApplicationUser applicationUser) throws HomeLocationNotSetException;
}
