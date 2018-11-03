package pl.edu.agh.geoxplore.service;

import pl.edu.agh.geoxplore.entity.ApplicationUser;
import pl.edu.agh.geoxplore.entity.HomeLocation;
import pl.edu.agh.geoxplore.exception.application.HomeLocationNotSetException;
import pl.edu.agh.geoxplore.rest.Geolocation;

public interface ILocalizationService {
    Geolocation getHomeLocation(ApplicationUser applicationUser) throws HomeLocationNotSetException;

    void setHomeLocation(HomeLocation homeLocation, ApplicationUser applicationUser);
}
