package pl.edu.agh.geoxplore.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class UserStatisticsServiceTest {

    UserStatisticsService service = new UserStatisticsService();

    @Test
    void getLevelFromExp() {
        long exp = 1000;
        long lvl = service.getLevelFromExp(exp);
        long backExp = service.calculateNeededExp(lvl);
        long backExpHigher = service.calculateNeededExp(lvl+1);
        assertTrue(backExp <= exp && exp <= backExpHigher);
    }
}