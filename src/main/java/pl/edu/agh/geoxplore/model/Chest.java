package pl.edu.agh.geoxplore.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Chest {
    private Double longitude;
    private Double latitude;
    private Boolean opened;
}
