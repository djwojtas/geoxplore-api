package pl.edu.agh.geoxplore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.agh.geoxplore.entity.HomeLocation;
import pl.edu.agh.geoxplore.exception.application.*;
import pl.edu.agh.geoxplore.message.DefaultResponse;
import pl.edu.agh.geoxplore.rest.PasswordChange;
import pl.edu.agh.geoxplore.rest.ChestResponse;
import pl.edu.agh.geoxplore.rest.Geolocation;
import pl.edu.agh.geoxplore.rest.OpenedChest;
import pl.edu.agh.geoxplore.rest.UserStatistics;
import pl.edu.agh.geoxplore.service.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    IUserStatisticsService userStatisticsService;

    @Autowired
    IChestService chestService;

    @Autowired
    IAvatarService avatarService;

    @Autowired
    IAuthenticationService authenticationService;

    @Autowired
    ILocalizationService localizationService;

    @PostMapping("/home")
    public DefaultResponse setHome(@RequestBody HomeLocation homeLocation) {
        localizationService.setHomeLocation(homeLocation, authenticationService.getAuthenticatedUser());
        return new DefaultResponse("success");
    }

    @GetMapping("/home")
    public Geolocation getHome() throws HomeLocationNotSetException {
        return localizationService.getHomeLocation(authenticationService.getAuthenticatedUser());
    }

    @GetMapping("/chests")
    public List<ChestResponse> getChests() {
        return chestService.getUserChests(authenticationService.getAuthenticatedUser());
    }

    @GetMapping("/statistics")
    public UserStatistics getStatistics() {
        return userStatisticsService.getUserStatistics(authenticationService.getAuthenticatedUser());
    }

    @PostMapping("/chest/open/{id}")
    private OpenedChest openChest(@PathVariable(name = "id") Long id) throws ChestDoesNotExistException, ChestAlreadyOpenedException {
        return chestService.openChest(authenticationService.getAuthenticatedUser(), id);
    }

    @PostMapping("/avatar")
    public DefaultResponse avatarUpload(@RequestParam("file") MultipartFile file) throws IOException {
        avatarService.saveCurrentUserAvatar(file);
        return new DefaultResponse("success");
    }

    @GetMapping(value =  "/avatar", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<Resource> avatarDownload() throws MalformedURLException, AvatarNotSetException, UserDoesNotExistException {
        Resource avatar = avatarService.getAvatarByUsername(authenticationService.getAuthenticatedUser().getUsername());
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + avatar.getFilename() + "\"").body(avatar);
    }
}


