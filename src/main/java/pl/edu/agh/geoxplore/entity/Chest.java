package pl.edu.agh.geoxplore.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "chests")
public class Chest {
    @Id
    @NotNull
    @SequenceGenerator(name="chests_seq", sequenceName="chests_seq", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chests_seq")
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
    private Date dateCreated;

    private Timestamp dateFound;

    @NotNull
    private Long value;
}
