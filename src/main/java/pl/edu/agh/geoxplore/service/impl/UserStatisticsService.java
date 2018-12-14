package pl.edu.agh.geoxplore.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.edu.agh.geoxplore.entity.ApplicationUser;
import pl.edu.agh.geoxplore.entity.Chest;
import pl.edu.agh.geoxplore.entity.Title;
import pl.edu.agh.geoxplore.repository.ApplicationUserRepository;
import pl.edu.agh.geoxplore.repository.ChestRepository;
import pl.edu.agh.geoxplore.repository.FriendRepository;
import pl.edu.agh.geoxplore.rest.ChestStats;
import pl.edu.agh.geoxplore.rest.RankingUser;
import pl.edu.agh.geoxplore.rest.UserStatistics;
import pl.edu.agh.geoxplore.service.IUserStatisticsService;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class UserStatisticsService implements IUserStatisticsService {

    @Autowired
    FriendRepository friendRepository;

    @Autowired
    ChestRepository chestRepository;

    @Autowired
    ApplicationUserRepository applicationUserRepository;

    private static final double LEVEL_STEEPNESS = 2;
    private static final long LEVEL_EXP = 100;

    @Override
    public UserStatistics getUserStatistics(ApplicationUser applicationUser) {
        List<Chest> chests = chestRepository.findByUserAndDateFoundIsNotNull(applicationUser);

        UserStatistics userStatistics = new UserStatistics();
        userStatistics.setUsername(applicationUser.getUsername());
        userStatistics.setExperience(applicationUser.getExperience());
        userStatistics.setLevel(applicationUser.getLevel());
        userStatistics.setToNextLevel(calculateLevelPercent(applicationUser.getExperience(), applicationUser.getLevel()));
        userStatistics.setFriends((long) applicationUser.getHaveFriends().size());
        userStatistics.setOpenedOverallChests((long) chests.size());
        userStatistics.setTitle(applicationUser.getTitle());
        userStatistics.setAchievements(new String[] {"10 skrzynek", "100 skrzynek", "30 dni z rzędu", "30 legendarnych skrzynek"});

        ChestStats chestStats = new ChestStats();
        chestStats.setOpenedOverallCommonChests(
                chests.stream().filter(c -> c.getValue().equals(1L)).count()
        );
        chestStats.setOpenedOverallRareChests(
                chests.stream().filter(c -> c.getValue().equals(2L)).count()
        );
        chestStats.setOpenedOverallEpicChests(
                chests.stream().filter(c -> c.getValue().equals(3L)).count()
        );
        chestStats.setOpenedOverallLegendaryChests(
                chests.stream().filter(c -> c.getValue().equals(4L)).count()
        );
        chestStats.setOpenedLastWeekChests(
                chests.stream().filter(c -> c.getDateFound().after(
                        Timestamp.valueOf(LocalDate.now().minusDays(7).atStartOfDay()))
                ).count()
        );

        userStatistics.setChestStats(chestStats);
        return userStatistics;
    }


    @Override
    public Long getLevelFromExp(Long exp) {
        return (long) Math.pow(((double)(exp + LEVEL_EXP)/(double)LEVEL_EXP), 1.0/LEVEL_STEEPNESS);
    }

    private Double calculateLevelPercent(Long exp, Long level) {
        return ((((double) exp)-calculateNeededExp(level))/(calculateNeededExp(level+1))) * 100;
    }

    @Override
    public Long calculateNeededExp(Long level) {
        return (long) (Math.pow(level, LEVEL_STEEPNESS) * LEVEL_EXP) - LEVEL_EXP;
    }

    @Override
    public long calculateExpFromChest(Chest chest) {
        return (long) (((double) chest.getValue()) * 10.0 * ((new Random()).nextDouble() + 0.5));
    }

    @Override
    public List<RankingUser> getRankingSortedAndPaged(Pageable pageable) {
        List<RankingUser> ranking = new ArrayList<>();

        int startingPlace = pageable.getPageNumber() * pageable.getPageSize();
        int currentPlace = 1;

        for(ApplicationUser applicationUser :
                applicationUserRepository.findAll(pageable)) {

            List<Chest> userChests = chestRepository.findByUserAndDateFoundIsNotNull(applicationUser);

            ranking.add(new RankingUser(
                    applicationUser.getUsername(),
                    applicationUser.getLevel(),
                    (long) userChests.size(),
                    userChests.stream()
                            .filter(c -> c.getDateFound().after(
                                    Timestamp.valueOf(LocalDate.now().minusDays(7).atStartOfDay()))).count(),
                    applicationUser.getTitle(),
                    (long) startingPlace + currentPlace,
                    new String[] {"10 skrzynek", "100 skrzynek", "30 dni z rzędu", "30 legendarnych skrzynek"}
            ));

            ++currentPlace;
        }

        return ranking;
    }

    @Override
    public Long gainExpFromChest(Chest chest, ApplicationUser applicationUser) {
        Long gainedExp = calculateExpFromChest(chest);
        applicationUser.setExperience(applicationUser.getExperience() + gainedExp);
        applicationUser.setLevel(getLevelFromExp(applicationUser.getExperience()));

        applicationUserRepository.save(applicationUser);

        return gainedExp;
    }

    @Override
    public void updateTitle(String title, ApplicationUser authenticatedUser) {
        ApplicationUser user = applicationUserRepository.findByUsername(authenticatedUser.getUsername());
        user.setTitle(title);
        applicationUserRepository.save(user);
    }
}
