package pl.edu.agh.geoxplore.service;

import org.springframework.data.domain.Pageable;
import pl.edu.agh.geoxplore.entity.ApplicationUser;
import pl.edu.agh.geoxplore.entity.Chest;
import pl.edu.agh.geoxplore.rest.RankingUser;
import pl.edu.agh.geoxplore.rest.UserStatistics;

import java.util.List;

public interface IUserStatisticsService {
    UserStatistics getUserStatistics(ApplicationUser applicationUser);

    Long getLevelFromExp(Long exp);

    Long calculateNeededExp(Long level);

    long calculateExpFromChest(Chest chest);

    List<RankingUser> getRankingSortedAndPaged(Pageable pageable);

    Long gainExpFromChest(Chest chest, ApplicationUser applicationUser);
}
