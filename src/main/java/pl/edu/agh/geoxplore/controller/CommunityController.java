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
import pl.edu.agh.geoxplore.exception.application.UserDoesntExistsException;
import pl.edu.agh.geoxplore.message.DefaultResponse;
import pl.edu.agh.geoxplore.rest.RankingUser;
import pl.edu.agh.geoxplore.service.AuthenticationService;
import pl.edu.agh.geoxplore.service.AvatarService;
import pl.edu.agh.geoxplore.service.FriendService;
import pl.edu.agh.geoxplore.service.UserStatisticsService;

import java.net.MalformedURLException;
import java.util.List;

@RestController
@RequestMapping("/community")
public class CommunityController {

    @Autowired
    UserStatisticsService userStatisticsService;

    @Autowired
    AvatarService avatarService;

    @Autowired
    FriendService friendService;

    @Autowired
    AuthenticationService authenticationService;

    @GetMapping("/ranking")
    List<RankingUser> getRanking(Pageable pageable) {
        return userStatisticsService.getRankingSortedAndPaged(pageable);
    }

    @GetMapping("/get-friends")
    public List<RankingUser> getFriends() {
        return friendService.getFriends(authenticationService.getAuthenticatedUser());
    }

    @PostMapping("/add-friend/{username}")
    public DefaultResponse addFriend(@PathVariable(name = "username") String username) throws FriendExistsException {
        friendService.addFriend(authenticationService.getAuthenticatedUser(), username);
        return new DefaultResponse("success");
    }

    @GetMapping(
            value =  "/avatar/{username}",
            produces = MediaType.IMAGE_PNG_VALUE
    )
    public ResponseEntity<Resource> avatarDownload(@PathVariable("username") String username) throws UserDoesntExistsException, AvatarNotSetException, MalformedURLException {
        Resource avatar = avatarService.getAvatarByUsername(username);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + avatar.getFilename() + "\"").body(avatar);
    }
}
