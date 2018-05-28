package pl.edu.agh.geoxplore.rest;

import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;

@Data
public class ChestResponse {
    private Long id;
    private Double longitude;
    private Double latitude;
    private Date dateCreated;
    private Timestamp dateFound;
    private Boolean opened;
    private Long value;
}
