package pl.edu.agh.geoxplore.model;

import lombok.Data;

@Data
public class UserStatistics {
    private String username;
    private Long experience;
    private Long level;
    private Double toNextLevel;
    private Long openedChests;
}
