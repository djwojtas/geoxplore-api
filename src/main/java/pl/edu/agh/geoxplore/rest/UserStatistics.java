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
    private Long friends;
    private Long openedOverallChests;
    private ChestStats chestStats;
    private String title;
    private String[] achievements = {"10 skrzynek", "100 skrzynek", "30 dni z rzędu", "30 legendarnych skrzynek"};
}
