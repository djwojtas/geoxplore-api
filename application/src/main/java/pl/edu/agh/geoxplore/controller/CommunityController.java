package pl.edu.agh.geoxplore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.geoxplore.exception.application.FriendExistsException;
import pl.edu.agh.geoxplore.message.DefaultResponse;
import pl.edu.agh.geoxplore.entity.ApplicationUser;
import pl.edu.agh.geoxplore.entity.Friend;
import pl.edu.agh.geoxplore.repository.ApplicationUserRepository;
import pl.edu.agh.geoxplore.repository.FriendRepository;
import pl.edu.agh.geoxplore.rest.RankingUser;
import pl.edu.agh.geoxplore.service.UserStatisticsService;

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

    @GetMapping("/ranking")
    List<RankingUser> getUserRanking() {
        List<RankingUser> ranking = new ArrayList<>();

        for(ApplicationUser applicationUser : applicationUserRepository.findAll()) {
            ranking.add(new RankingUser(
                    applicationUser.getUsername(),
                    applicationUser.getLevel(),
                    (long) (Math.random()*50) //todo remove mock
            ));
        }

        return ranking;
    }

    @GetMapping("/get-friends")
    List<RankingUser> getFriends() {
        return getAuthenticatedUser().getHaveFriends().stream()
                    .map(Friend::getFriend)
                    .map(f -> new RankingUser(f.getUsername(), f.getLevel(), (long) (Math.random()*50))) //todo remove mock
                    .collect(Collectors.toList());
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
