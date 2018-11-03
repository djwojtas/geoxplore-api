package pl.edu.agh.geoxplore.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.geoxplore.entity.ApplicationUser;
import pl.edu.agh.geoxplore.entity.HomeLocation;
import pl.edu.agh.geoxplore.exception.application.HomeLocationNotSetException;
import pl.edu.agh.geoxplore.repository.HomeLocationRepository;
import pl.edu.agh.geoxplore.rest.Geolocation;
import pl.edu.agh.geoxplore.service.ILocalizationService;

import java.sql.Timestamp;
import java.util.Optional;

@Service
public class LocalizationService implements ILocalizationService {

    @Autowired
    HomeLocationRepository homeLocationRepository;

    @Override
    public Geolocation getHomeLocation(ApplicationUser applicationUser) throws HomeLocationNotSetException {
        Optional<HomeLocation> homeLocation = Optional.ofNullable(homeLocationRepository.findFirstByUserOrderByDateAddedDesc(applicationUser));
        if(!homeLocation.isPresent()) throw new HomeLocationNotSetException();
        return new Geolocation(homeLocation.get().getLongitude(), homeLocation.get().getLatitude());
    }

    @Override
    public void setHomeLocation(HomeLocation homeLocation, ApplicationUser applicationUser) {
        homeLocation.setDateAdded(new Timestamp(System.currentTimeMillis()));
        homeLocation.setUser(applicationUser);
        homeLocationRepository.save(homeLocation);
    }
}
