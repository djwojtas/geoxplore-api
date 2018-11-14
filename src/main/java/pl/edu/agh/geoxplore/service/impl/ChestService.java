package pl.edu.agh.geoxplore.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.geoxplore.entity.ApplicationUser;
import pl.edu.agh.geoxplore.entity.Chest;
import pl.edu.agh.geoxplore.entity.HomeLocation;
import pl.edu.agh.geoxplore.exception.application.ChestAlreadyOpenedException;
import pl.edu.agh.geoxplore.exception.application.ChestDoesNotExistException;
import pl.edu.agh.geoxplore.mapper.ChestMapper;
import pl.edu.agh.geoxplore.model.Point;
import pl.edu.agh.geoxplore.repository.ApplicationUserRepository;
import pl.edu.agh.geoxplore.repository.ChestRepository;
import pl.edu.agh.geoxplore.repository.HomeLocationRepository;
import pl.edu.agh.geoxplore.rest.ChestResponse;
import pl.edu.agh.geoxplore.rest.OpenedChest;
import pl.edu.agh.geoxplore.service.IChestService;
import pl.edu.agh.geoxplore.service.IUserStatisticsService;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChestService implements IChestService {
    private final double LATITUDE_PER_KM = 0.00904371732957;
    private final double LONGITUDE_PER_KM = 0.00898311174991;

    @Autowired
    ChestRepository chestRepository;

    @Autowired
    IUserStatisticsService userStatisticsService;

    @Autowired
    ApplicationUserRepository applicationUserRepository;

    @Autowired
    ChestMapper chestMapper;

    @Autowired
    HomeLocationRepository homeLocationRepository;

    private List<Point> getRandomPointList(int numberOfChests, double lat, double lon, double distanceBetweenInM, double radiusInKM) {
        List<Point> pointList = new ArrayList<>();

        Point p;
        for (int i = 0; i < numberOfChests; ++i) {
            p = getRandomPointAround(lat, lon, radiusInKM);
            while (checkIfTooClose(p, pointList, distanceBetweenInM)) {
                p = getRandomPointAround(lat, lon, radiusInKM);
            }
            pointList.add(p);
        }

        return pointList;
    }

    private boolean checkIfTooClose(Point testPoint, List<Point> testList, double distanceInM) {
        return testList.stream().anyMatch(point ->
                checkDistanceBetween(point.getLatitude(), point.getLongitude(), testPoint.getLatitude(), testPoint.getLongitude()) <= distanceInM);
    }

    private Point getRandomPointAround(double latitude, double longitude, double distanceInKM) {
        double randomDegree = Math.random() * Math.PI * 2;
        double randomValue = Math.random() * distanceInKM;

        double randomLat = latitude + (LATITUDE_PER_KM * Math.cos(randomDegree) * randomValue);
        double randomLong = longitude + (LONGITUDE_PER_KM * Math.sin(randomDegree) * randomValue * Math.cos(Math.toRadians(randomLat)));

        return new Point(randomLat, randomLong);
    }

    //returns meters
    private double checkDistanceBetween(double lat1, double lat2, double lon1, double lon2) {
        double R = 6371000;
        double radianLat1 = Math.toRadians(lat1);
        double radianLat2 = Math.toRadians(lat2);
        double deltaLat = Math.toRadians(lat2 - lat1);
        double deltaLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(radianLat1) * Math.cos(radianLat2) *
                        Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    private long getChestLevel() {
        double roll = Math.random();
        //todo b4release fix magic numbers to non-mock
        //0.6 0.9 0.975
        if(roll < 0.25) {
            return 1;
        } else if(roll < 0.5) {
            return 2;
        } else if(roll < 0.75) {
            return 3;
        } else {
            return 4;
        }
    }

    @Override
    public OpenedChest openChest(ApplicationUser applicationUser, Long id) throws ChestDoesNotExistException, ChestAlreadyOpenedException {
        Optional<Chest> chest = chestRepository.findById(id);

        if(chest.isPresent()) {
            if(chest.get().getDateFound() != null) throw new ChestAlreadyOpenedException();

            chest.get().setDateFound(new Timestamp(System.currentTimeMillis()));
            chestRepository.save(chest.get());

            Long gainedExp = userStatisticsService.calculateExpFromChest(chest.get());

            OpenedChest response = new OpenedChest();
            response.setExpGained(gainedExp);
            return response;
        } else {
            throw new ChestDoesNotExistException();
        }
    }

    @Override
    public List<ChestResponse> getUserChests(ApplicationUser applicationUser) {
        List<Chest> chests = chestRepository.findByUserAndDateCreated(applicationUser, new Date(System.currentTimeMillis()));

        if(chests.size() == 0) {
            HomeLocation homeLocation = homeLocationRepository.findFirstByUserOrderByDateAddedDesc(applicationUser);
            List<Point> randomPoints = getRandomPointList(3, homeLocation.getLatitude(), homeLocation.getLongitude(), 60, 1);

            randomPoints.forEach(
                    randomPoint -> chestRepository.save(
                            Chest.builder()
                            .id(-1L)
                            .user(applicationUser)
                            .longitude(randomPoint.getLongitude())
                            .latitude(randomPoint.getLatitude())
                            .dateCreated(new Date(System.currentTimeMillis()))
                            .value(getChestLevel())
                            .build())
            );

            //todo b4release remove mock for fronts
            Chest c = new Chest(-1L, applicationUser, homeLocation.getLongitude() - 0.00024, homeLocation.getLatitude() - 0.00024, new Date(System.currentTimeMillis()), null, getChestLevel());
            chestRepository.save(c);

            chests = chestRepository.findByUserAndDateCreated(applicationUser, new Date(System.currentTimeMillis()));
        }

        return chestMapper.ChestToResponse(chests);
    }
}