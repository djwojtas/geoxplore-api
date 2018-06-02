package pl.edu.agh.geoxplore.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserStatistics {
    private String username;
    private Long experience;
    private Long level;
    private Double toNextLevel;
    private Long openedOverallChests;
    private Long openedOverallCommonChests;
    private Long openedOverallRareChests;
    private Long openedOverallEpicChests;
    private Long openedOverallLegendaryChests;
    private Long openedLastWeekChests;
}
