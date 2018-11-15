package pl.edu.agh.geoxplore.service;

import org.springframework.data.domain.Pageable;
import pl.edu.agh.geoxplore.entity.ApplicationUser;
import pl.edu.agh.geoxplore.exception.application.FriendExistsException;
import pl.edu.agh.geoxplore.exception.application.SearchStringTooShortException;
import pl.edu.agh.geoxplore.rest.RankingUser;

import java.util.List;

public interface IFriendService {
    void addFriend(ApplicationUser currentUser, String usernameToAddAsFriend) throws FriendExistsException;

    List<RankingUser> getFriends(ApplicationUser applicationUser, Pageable pageable);

    List<String> searchFriend(String usernamePart) throws SearchStringTooShortException;
}
