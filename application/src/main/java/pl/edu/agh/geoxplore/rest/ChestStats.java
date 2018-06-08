package pl.edu.agh.geoxplore.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChestStats {
    private Long openedOverallCommonChests;
    private Long openedOverallRareChests;
    private Long openedOverallEpicChests;
    private Long openedOverallLegendaryChests;
    private Long openedLastWeekChests;
}
