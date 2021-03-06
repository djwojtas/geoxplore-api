package pl.edu.agh.geoxplore.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

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
@Builder
public class ApplicationUser {
    @Id
    @NotNull
    @SequenceGenerator(name="users_seq", sequenceName="users_seq", allocationSize=1)
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

    @Column(name = "title")
    @NotNull
    private String title;

    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="user")
    private List<HomeLocation> homeLocations;

    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="user")
    private List<Chest> chests;

    @OneToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL, mappedBy="user")
    private List<Friend> haveFriends;

    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="friend")
    private List<Friend> isFriend;

    @JsonIgnore
    public void setLevel(Long level) {
        this.level = level;
    }

    @JsonIgnore
    public void setExperience(Long experience) {
        this.experience = experience;
    }

    @JsonIgnore
    public void setHomeLocations(List<HomeLocation> homeLocations) {
        this.homeLocations = homeLocations;
    }

    @JsonIgnore
    public void setChests(List<Chest> chests) {
        this.chests = chests;
    }
}