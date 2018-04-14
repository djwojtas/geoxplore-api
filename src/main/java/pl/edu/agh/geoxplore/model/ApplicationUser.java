package pl.edu.agh.geoxplore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ApplicationUser {
    @Id
    @NotNull
    @SequenceGenerator(name="users_seq", sequenceName="users_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
    @JsonIgnore
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
}