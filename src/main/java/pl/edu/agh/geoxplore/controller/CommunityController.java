package pl.edu.agh.geoxplore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.geoxplore.exception.application.AvatarNotSetException;
import pl.edu.agh.geoxplore.exception.application.FriendExistsException;
import pl.edu.agh.geoxplore.exception.application.SearchStringTooShortException;
import pl.edu.agh.geoxplore.exception.application.UserDoesNotExistException;
import pl.edu.agh.geoxplore.message.DefaultResponse;
import pl.edu.agh.geoxplore.rest.RankingUser;
import pl.edu.agh.geoxplore.service.IAuthenticationService;
import pl.edu.agh.geoxplore.service.IAvatarService;
import pl.edu.agh.geoxplore.service.IFriendService;
import pl.edu.agh.geoxplore.service.IUserStatisticsService;

import java.net.MalformedURLException;
import java.util.List;

@RestController
@RequestMapping("/community")
public class CommunityController {

    @Autowired
    IUserStatisticsService userStatisticsService;

    @Autowired
    IAvatarService avatarService;

    @Autowired
    IFriendService friendService;

    @Autowired
    IAuthenticationService authenticationService;

    @GetMapping("/ranking")
    public List<RankingUser> getRanking(Pageable pageable) {
        return userStatisticsService.getRankingSortedAndPaged(pageable);
    }

    @GetMapping("/friend/find/{usernamePart}")
    public List<String> searchFriends(@PathVariable(name = "usernamePart") String usernamePart) throws SearchStringTooShortException {
        return friendService.searchFriend(usernamePart);
    }

    @GetMapping("/friends")
    public List<RankingUser> getFriends(Pageable pageable) {
        return friendService.getFriends(authenticationService.getAuthenticatedUser(), pageable);
    }

    @PostMapping("/friend/add/{username}")
    public DefaultResponse addFriend(@PathVariable(name = "username") String username) throws FriendExistsException {
        friendService.addFriend(authenticationService.getAuthenticatedUser(), username);
        return new DefaultResponse("success");
    }

    @GetMapping(
            value =  "/avatar/{username}",
            produces = MediaType.IMAGE_PNG_VALUE
    )
    public ResponseEntity<Resource> avatarDownload(@PathVariable("username") String username) throws UserDoesNotExistException, AvatarNotSetException, MalformedURLException {
        Resource avatar = avatarService.getAvatarByUsername(username);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + avatar.getFilename() + "\"").body(avatar);
    }
}
