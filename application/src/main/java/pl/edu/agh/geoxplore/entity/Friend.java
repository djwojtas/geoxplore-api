package pl.edu.agh.geoxplore.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "friends")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Friend {
    @Id
    @NotNull
    @SequenceGenerator(name="friends_seq", sequenceName="friends_seq", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "friends_seq")
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name="user_id")
    private ApplicationUser user;

    @NotNull
    @ManyToOne
    @JoinColumn(name="friend_id")
    private ApplicationUser friend;
}