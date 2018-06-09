package pl.edu.agh.geoxplore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.agh.geoxplore.entity.ApplicationUser;
import pl.edu.agh.geoxplore.entity.Chest;
import pl.edu.agh.geoxplore.entity.HomeLocation;
import pl.edu.agh.geoxplore.exception.application.AvatarNotSetException;
import pl.edu.agh.geoxplore.exception.application.HomeLocationNotSetException;
import pl.edu.agh.geoxplore.exception.application.UserDoesntExistsException;
import pl.edu.agh.geoxplore.mapper.ChestMapper;
import pl.edu.agh.geoxplore.message.DefaultResponse;
import pl.edu.agh.geoxplore.model.Point;
import pl.edu.agh.geoxplore.repository.ApplicationUserRepository;
import pl.edu.agh.geoxplore.repository.ChestRepository;
import pl.edu.agh.geoxplore.repository.HomeLocationRepository;
import pl.edu.agh.geoxplore.rest.ChestResponse;
import pl.edu.agh.geoxplore.rest.Geolocation;
import pl.edu.agh.geoxplore.rest.OpenedChest;
import pl.edu.agh.geoxplore.rest.UserStatistics;
import pl.edu.agh.geoxplore.service.*;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserStatisticsService userStatisticsService;

    @Autowired
    ChestService chestService;

    @Autowired
    AvatarService avatarService;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    LocalizationService localizationService;

    @PostMapping("/set-home")
    DefaultResponse home(@RequestBody HomeLocation homeLocation) {
        localizationService.setHomeLocation(homeLocation, authenticationService.getAuthenticatedUser());
        return new DefaultResponse("success");
    }

    @GetMapping("/chests")
    List<ChestResponse> getChests() {
        return chestService.getUserChests(authenticationService.getAuthenticatedUser());
    }

    @GetMapping("/my-statistics")
    UserStatistics getMyStatistics() {
        return userStatisticsService.getUserStatistics(authenticationService.getAuthenticatedUser());
    }

    @PostMapping("/open-chest/{id}")
    private OpenedChest openChest(@PathVariable(name = "id") Long id) {
        return chestService.openChest(authenticationService.getAuthenticatedUser(), id);
    }

    //todo change mappings to actually rest wihout get- set-
    @GetMapping("/get-home")
    public Geolocation getHomeLocation() throws HomeLocationNotSetException {
        return localizationService.getHomeLocation(authenticationService.getAuthenticatedUser());
    }

    @PostMapping("/avatar")
    public DefaultResponse avatarUpload(@RequestParam("file") MultipartFile file) throws IOException {
        avatarService.saveCurrentUserAvatar(file);
        return new DefaultResponse("success");
    }

    @GetMapping(
            value =  "/avatar",
            produces = MediaType.IMAGE_PNG_VALUE
    )
    public ResponseEntity<Resource> avatarDownload() throws MalformedURLException, AvatarNotSetException, UserDoesntExistsException {
        Resource avatar = avatarService.getAvatarByUsername(authenticationService.getAuthenticatedUser().getUsername());
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + avatar.getFilename() + "\"").body(avatar);
    }
}


