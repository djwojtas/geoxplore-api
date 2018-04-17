package pl.edu.agh.geoxplore.service;

import org.springframework.stereotype.Service;
import pl.edu.agh.geoxplore.model.ApplicationUser;
import pl.edu.agh.geoxplore.model.UserStatistics;

@Service
public class UserStatisticsService {
    private static final double LEVEL_STEEPNESS = 1.5;
    private static final long LEVEL_EXP = 100;

    public UserStatistics getUserStatistics(ApplicationUser applicationUser) {
        UserStatistics userStatistics = new UserStatistics();
        userStatistics.setUsername(applicationUser.getUsername());
        userStatistics.setExperience(applicationUser.getExperience());
        userStatistics.setLevel(applicationUser.getLevel());
        userStatistics.setToNextLevel(calculateLevelProcent(applicationUser.getExperience(), applicationUser.getLevel()));
        userStatistics.setOpenedChests((long) (Math.random()*50));
        return  userStatistics;
    }

    private Double calculateLevelProcent(Long exp, Long level) {
        return ((double) exp)/(calculateNeededExp(level)) * 100;
    }

    private Long calculateNeededExp(Long level) {
        return (long) Math.sqrt(Math.pow(level, LEVEL_STEEPNESS)) * LEVEL_EXP;
    }
}
