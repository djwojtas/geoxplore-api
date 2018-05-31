package pl.edu.agh.geoxplore.service;

import org.springframework.stereotype.Service;
import pl.edu.agh.geoxplore.entity.ApplicationUser;
import pl.edu.agh.geoxplore.rest.UserStatistics;

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
        userStatistics.setOpenedChests((long) (Math.random()*50)); //todo remove mock
        return userStatistics;
    }

    public Long getLevelFromExp(Long exp) {
        return (long) Math.pow(((double)(exp + LEVEL_EXP)/(double)LEVEL_EXP), 2L/LEVEL_STEEPNESS);
    }

    private Double calculateLevelProcent(Long exp, Long level) {
        return ((((double) exp)-calculateNeededExp(level))/(calculateNeededExp(level+1))) * 100;
    }

    public Long calculateNeededExp(Long level) {
        return (long) (Math.sqrt(Math.pow(level, LEVEL_STEEPNESS)) * LEVEL_EXP) - LEVEL_EXP;
    }
}
