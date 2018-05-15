package pl.edu.agh.geoxplore.model;

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
    private Long openedChests;
}
