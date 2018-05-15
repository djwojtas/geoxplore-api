package pl.edu.agh.geoxplore.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RankingUser {
    private String username;
    private Long level;
    private Long openedChests;
}
