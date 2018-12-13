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
    private String title;
    private Long place;
    private String[] achievements = {"10 skrzynek", "100 skrzynek", "30 dni z rzędu", "30 legendarnych skrzynek"};
}
