package pl.edu.agh.geoxplore.service;

import org.junit.jupiter.api.Test;
import pl.edu.agh.geoxplore.service.impl.UserStatisticsService;

import static org.junit.jupiter.api.Assertions.assertTrue;

class UserStatisticsServiceTest {

    UserStatisticsService service = new UserStatisticsService();

    @Test
    void shouldExpectMoreExpForHigherLevel() {
        long exp = 1000;
        long lvl = service.getLevelFromExp(exp);

        long lowerLevelExp = service.calculateNeededExp(lvl);
        long higherLevelExp = service.calculateNeededExp(lvl+1);

        assertTrue(lowerLevelExp <= exp && exp <= higherLevelExp);
    }
}