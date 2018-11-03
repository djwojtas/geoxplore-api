package pl.edu.agh.geoxplore.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.geoxplore.entity.ApplicationUser;
import pl.edu.agh.geoxplore.entity.Chest;
import pl.edu.agh.geoxplore.entity.Friend;
import pl.edu.agh.geoxplore.exception.application.FriendExistsException;
import pl.edu.agh.geoxplore.repository.ApplicationUserRepository;
import pl.edu.agh.geoxplore.repository.ChestRepository;
import pl.edu.agh.geoxplore.rest.RankingUser;
import pl.edu.agh.geoxplore.service.IFriendService;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

//todo create interfaces for services
@Service
public class FriendService implements IFriendService {

    @Autowired
    ApplicationUserRepository applicationUserRepository;

    @Autowired
    ChestRepository chestRepository;

    @Override
    public void addFriend(ApplicationUser currentUser, String usernameToAddAsFriend) throws FriendExistsException {
        ApplicationUser friend = applicationUserRepository.findByUsername(usernameToAddAsFriend);

        if(currentUser.getHaveFriends().stream().anyMatch(f -> f.getFriend().getId().equals(friend.getId()))) {
            throw new FriendExistsException();
        }

        Friend newFriend = new Friend();
        newFriend.setUser(currentUser);
        newFriend.setFriend(friend);

        currentUser.getHaveFriends().add(newFriend);
        applicationUserRepository.save(currentUser);
    }

    @Override
    public List<RankingUser> getFriends(ApplicationUser applicationUser) {
        return applicationUser.getHaveFriends().stream()
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
}
