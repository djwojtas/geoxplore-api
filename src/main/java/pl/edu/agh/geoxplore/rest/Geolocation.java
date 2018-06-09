package pl.edu.agh.geoxplore.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Geolocation {
    Double longitude;
    Double latitude;
}
