package pl.edu.agh.geoxplore.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity
@Table(name = "home_locations")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class HomeLocation {
    @Id
    @NotNull
    @SequenceGenerator(name="home_locations_seq", sequenceName="home_locations_seq", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "home_locations_seq")
    @JsonIgnore
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name="user_id")
    private ApplicationUser user;

    @NotNull
    private Double longitude;

    @NotNull
    private Double latitude;

    @NotNull
    private Timestamp dateAdded;
}
