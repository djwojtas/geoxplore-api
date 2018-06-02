package pl.edu.agh.geoxplore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.geoxplore.entity.ApplicationUser;
import pl.edu.agh.geoxplore.entity.Chest;
import pl.edu.agh.geoxplore.entity.Friend;
import pl.edu.agh.geoxplore.exception.application.FriendExistsException;
import pl.edu.agh.geoxplore.message.DefaultResponse;
import pl.edu.agh.geoxplore.repository.ApplicationUserRepository;
import pl.edu.agh.geoxplore.repository.ChestRepository;
import pl.edu.agh.geoxplore.repository.FriendRepository;
import pl.edu.agh.geoxplore.rest.RankingUser;
import pl.edu.agh.geoxplore.service.UserStatisticsService;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/community")
public class CommunityController {

    @Autowired
    ApplicationUserRepository applicationUserRepository;

    @Autowired
    FriendRepository friendRepository;

    @Autowired
    UserStatisticsService userStatisticsService;

    @Autowired
    ChestRepository chestRepository;

    @GetMapping("/ranking")
    List<RankingUser> getUserRanking() {
        List<RankingUser> ranking = new ArrayList<>();

        for(ApplicationUser applicationUser : applicationUserRepository.findAll()) {
            List<Chest> userChests = chestRepository.findByUserAndDateFoundIsNotNull(applicationUser);

            ranking.add(new RankingUser(
                    applicationUser.getUsername(),
                    applicationUser.getLevel(),
                    (long) userChests.size(),
                    userChests.stream()
                            .filter(c -> c.getDateFound().after(
                                    Timestamp.valueOf(LocalDate.now().minusDays(7).atStartOfDay()))).count()
            ));
        }

        return ranking;
    }

    @GetMapping("/get-friends")
    List<RankingUser> getFriends() {
        return getAuthenticatedUser().getHaveFriends().stream()
                    .map(Friend::getFriend)
                    .map(this::mapApplicationUserToRankingUser) //todo remove mock
                    .collect(Collectors.toList());
    }

    private RankingUser mapApplicationUserToRankingUser(ApplicationUser user) {
        List<Chest> chests = chestRepository.findByUserAndDateFoundIsNotNull(user);

        RankingUser rankingUser = new RankingUser();
        rankingUser.setUsername(user.getUsername());
        rankingUser.setLevel(user.getLevel());
        rankingUser.setOpenedChests((long) chests.size());
        rankingUser.setLastWeekChests(
                chests.stream()
                        .filter(c -> c.getDateFound().after(
                            Timestamp.valueOf(LocalDate.now().minusDays(7).atStartOfDay()))
                ).count()
        );

        return rankingUser;
    }

    @PostMapping("/add-friend/{username}")
    DefaultResponse addFriend(@PathVariable(name = "username") String username) throws FriendExistsException {
        ApplicationUser user = getAuthenticatedUser();
        ApplicationUser friend = applicationUserRepository.findByUsername(username);

        if(user.getHaveFriends().stream().anyMatch(f -> f.getFriend().getId().equals(friend.getId()))) {
            throw new FriendExistsException();
        }

        Friend newFriend = new Friend();
        newFriend.setUser(user);
        newFriend.setFriend(friend);

        user.getHaveFriends().add(newFriend);
        applicationUserRepository.save(user);

        return new DefaultResponse("success");
    }

    private ApplicationUser getAuthenticatedUser() {
        return (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
