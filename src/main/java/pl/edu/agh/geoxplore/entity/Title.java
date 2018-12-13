package pl.edu.agh.geoxplore.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.sql.Timestamp;

//@NoArgsConstructor
//@AllArgsConstructor
@Getter
@Setter
//@Entity
//@Builder
//@Table(name = "titles")
public class Title { //todo create rest objects for all entities
//    @Id
//    @NotNull
//    @SequenceGenerator(name="titles_seq", sequenceName="titles_seq", allocationSize=1)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "titles_seq")
//    private Long id;
//
//    @NotNull
//    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="user")
//    @JsonIgnore
//    private ApplicationUser user;
//
    private String title;
}
