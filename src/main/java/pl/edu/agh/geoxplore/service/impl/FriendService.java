package pl.edu.agh.geoxplore.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.edu.agh.geoxplore.entity.ApplicationUser;
import pl.edu.agh.geoxplore.entity.Chest;
import pl.edu.agh.geoxplore.entity.Friend;
import pl.edu.agh.geoxplore.exception.application.FriendExistsException;
import pl.edu.agh.geoxplore.exception.application.SearchStringTooShortException;
import pl.edu.agh.geoxplore.repository.ApplicationUserRepository;
import pl.edu.agh.geoxplore.repository.ChestRepository;
import pl.edu.agh.geoxplore.rest.RankingUser;
import pl.edu.agh.geoxplore.service.IFriendService;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FriendService implements IFriendService {

    @Autowired
    ApplicationUserRepository applicationUserRepository;

    @Autowired
    ChestRepository chestRepository;

    @Override
    public void addFriend(ApplicationUser currentUser, String usernameToAddAsFriend) throws FriendExistsException {
        ApplicationUser friend = applicationUserRepository.findByUsername(usernameToAddAsFriend);

        if(currentUser.getHaveFriends().stream()
                .anyMatch(f -> f.getFriend().getId().equals(friend.getId()))) {
            throw new FriendExistsException();
        }

        Friend newFriend = new Friend();
        newFriend.setUser(currentUser);
        newFriend.setFriend(friend);

        currentUser.getHaveFriends().add(newFriend);
        applicationUserRepository.save(currentUser);
    }

    @Override
    public List<RankingUser> getFriends(ApplicationUser applicationUser, Pageable pageable) {
        return applicationUserRepository.findAllFriends(applicationUser.getId(), pageable).stream()
                .map(this::mapApplicationUserToRankingUser)
                .collect(Collectors.toList());

//        return applicationUser.getHaveFriends().stream()
//                .map(Friend::getFriend)
//                .map(this::mapApplicationUserToRankingUser)
//                .collect(Collectors.toList());
    }

    @Override
    public List<String> searchFriend(String usernamePart) throws SearchStringTooShortException {
        if(usernamePart.length() < 3) throw new SearchStringTooShortException();

        return applicationUserRepository.findAllByUsernameContainingIgnoreCase(usernamePart).stream()
                .map(ApplicationUser::getUsername)
                .collect(Collectors.toList());
    }

    private RankingUser mapApplicationUserToRankingUser(ApplicationUser user) {
        List<Chest> chests = chestRepository.findByUserAndDateFoundIsNotNull(user);

        return RankingUser.builder()
                .username(user.getUsername())
                .level(user.getLevel())
                .openedChests((long) chests.size())
                .lastWeekChests(chests.stream()
                        .filter(chest -> chest.getDateFound().after(
                                Timestamp.valueOf(LocalDate.now().minusDays(7).atStartOfDay()))
                        ).count())
                .title(user.getTitle()) //todo
                .achievements(new String[] {"10 skrzynek", "100 skrzynek", "30 dni z rzędu", "30 legendarnych skrzynek"})
                .build();
    }
}
