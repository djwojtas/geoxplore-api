package pl.edu.agh.geoxplore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ApplicationUser {
    @Id
    @NotNull
    @SequenceGenerator(name="users_seq", sequenceName="users_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
    private Long id;

    @NotNull
    @Column(name = "username")
    @Size(min = 4, max = 16, message = "Username should be between 4 and 16 characters long")
    private String username;

    @NotNull
    @Column(name = "password")
    private String password;

    @NotNull
    @Size(max = 254)
    @Column(name = "email")
    @Email(message = "Invalid email")
    private String email;

    @NotNull
    private Long level;

    @NotNull
    private Long experience;

    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="user")
    private List<HomeLocation> home_locations;

    @JsonIgnore
    public void setLevel(Long level) {
        this.level = level;
    }

    @JsonIgnore
    public void setExperience(Long experience) {
        this.experience = experience;
    }

    @JsonIgnore
    public void setHome_locations(List<HomeLocation> home_locations) {
        this.home_locations = home_locations;
    }
}