package pl.edu.agh.geoxplore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.geoxplore.entity.ApplicationUser;
import pl.edu.agh.geoxplore.entity.Chest;
import pl.edu.agh.geoxplore.entity.Friend;
import pl.edu.agh.geoxplore.exception.application.AvatarNotSetException;
import pl.edu.agh.geoxplore.exception.application.FriendExistsException;
import pl.edu.agh.geoxplore.exception.application.UserDoesntExistsException;
import pl.edu.agh.geoxplore.message.DefaultResponse;
import pl.edu.agh.geoxplore.repository.ApplicationUserRepository;
import pl.edu.agh.geoxplore.repository.ChestRepository;
import pl.edu.agh.geoxplore.repository.FriendRepository;
import pl.edu.agh.geoxplore.rest.RankingUser;
import pl.edu.agh.geoxplore.service.AuthenticationService;
import pl.edu.agh.geoxplore.service.AvatarService;
import pl.edu.agh.geoxplore.service.FriendService;
import pl.edu.agh.geoxplore.service.UserStatisticsService;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
