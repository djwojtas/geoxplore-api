package pl.edu.agh.geoxplore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.geoxplore.entity.ApplicationUser;
import pl.edu.agh.geoxplore.entity.Chest;
import pl.edu.agh.geoxplore.repository.ChestRepository;
import pl.edu.agh.geoxplore.rest.UserStatistics;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@Service
public class UserStatisticsService {
    @Autowired
    ChestRepository chestRepository;

    private static final double LEVEL_STEEPNESS = 1.5;
    private static final long LEVEL_EXP = 100;

    public UserStatistics getUserStatistics(ApplicationUser applicationUser) {
        List<Chest> chests = chestRepository.findByUserAndDateFoundIsNotNull(applicationUser);

        UserStatistics userStatistics = new UserStatistics();
        userStatistics.setUsername(applicationUser.getUsername());
        userStatistics.setExperience(applicationUser.getExperience());
        userStatistics.setLevel(applicationUser.getLevel());
        userStatistics.setToNextLevel(calculateLevelProcent(applicationUser.getExperience(), applicationUser.getLevel()));
        userStatistics.setOpenedOverallChests((long) chests.size());
        userStatistics.setOpenedOverallCommonChests(
                chests.stream().filter(c -> c.getValue().equals(1L)).count()
        );
        userStatistics.setOpenedOverallRareChests(
                chests.stream().filter(c -> c.getValue().equals(2L)).count()
        );
        userStatistics.setOpenedOverallEpicChests(
                chests.stream().filter(c -> c.getValue().equals(3L)).count()
        );
        userStatistics.setOpenedOverallLegendaryChests(
                chests.stream().filter(c -> c.getValue().equals(4L)).count()
        );
        userStatistics.setOpenedLastWeekChests(
                chests.stream().filter(c -> c.getDateFound().after(
                        Timestamp.valueOf(LocalDate.now().minusDays(7).atStartOfDay()))
                ).count()
        );

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
