package pl.edu.agh.geoxplore.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RankingUser {
    private String username;
    private Long level;
    private Long openedChests;
    private Long lastWeekChests;
    private Long place;
}
