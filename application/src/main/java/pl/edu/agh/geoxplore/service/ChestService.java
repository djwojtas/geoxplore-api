package pl.edu.agh.geoxplore.service;

import org.springframework.stereotype.Service;
import pl.edu.agh.geoxplore.model.Point;

import java.util.ArrayList;
import java.util.List;

//todo add interface and impl to all services
//FIXME this class begs for refactor and algorithms optimisation
@Service
public class ChestService {
    private final double LATITUDE_PER_KM = 0.00904371732957;
    private final double LONGITUDE_PER_KM = 0.00898311174991;

    public List<Point> getRandomPointList(int numberOfChests, double lat, double lon, double distanceBetweenInM, double radiusInKM) {
        List<Point> pointList = new ArrayList<>();

        Point p;
        for (int i = 0; i < numberOfChests; ++i) {
            p = getRandomPointAround(lat, lon, radiusInKM);
            if (!checkIfTooClose(p, pointList, distanceBetweenInM)) {
                pointList.add(p);
            } else {
                --i;
            }
        }

        return pointList;
    }

    private boolean checkIfTooClose(Point testPoint, List<Point> testList, double distanceInM) {
        for (Point p : testList) {
            if (checkDistanceBetween(p.getLatitude(), p.getLongitude(), testPoint.getLatitude(), testPoint.getLongitude()) <= distanceInM) {
                return true;
            }
        }
        return false;
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
}