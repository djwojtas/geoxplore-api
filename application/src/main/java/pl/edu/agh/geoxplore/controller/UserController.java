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
import pl.edu.agh.geoxplore.mapper.ChestMapper;
import pl.edu.agh.geoxplore.message.DefaultResponse;
import pl.edu.agh.geoxplore.model.Point;
import pl.edu.agh.geoxplore.repository.ApplicationUserRepository;
import pl.edu.agh.geoxplore.repository.ChestRepository;
import pl.edu.agh.geoxplore.repository.HomeLocationRepository;
import pl.edu.agh.geoxplore.rest.ChestResponse;
import pl.edu.agh.geoxplore.rest.OpenedChest;
import pl.edu.agh.geoxplore.rest.UserStatistics;
import pl.edu.agh.geoxplore.service.ChestService;
import pl.edu.agh.geoxplore.service.UserStatisticsService;

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
    HomeLocationRepository homeLocationRepository;

    @Autowired
    UserStatisticsService userStatisticsService;

    @Autowired
    ChestRepository chestRepository;

    @Autowired
    ApplicationUserRepository applicationUserRepository;

    @Autowired
    ChestMapper chestMapper;

    @Autowired
    ChestService chestService;

    //todo i really REALLY need to move logic to service layer
    private ApplicationUser getAuthenticatedUser() {
        return (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @PostMapping("/set-home")
    DefaultResponse home(@RequestBody HomeLocation homeLocation) {
        homeLocation.setDateAdded(new Timestamp(System.currentTimeMillis()));
        homeLocation.setUser(getAuthenticatedUser());
        homeLocationRepository.save(homeLocation);

        return new DefaultResponse("success");
    }

    @GetMapping("/chests")
    List<ChestResponse> getChests() {
        ApplicationUser user = (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Chest> chests = chestRepository.findByUserAndDateCreated(user, new Date(System.currentTimeMillis()));

        if(chests.size() == 0) {
            HomeLocation homeLocation = homeLocationRepository.findFirstByUserOrderByDateAddedDesc(user);
            List<Point> randomPoints = chestService.getRandomPointList(3, homeLocation.getLatitude(), homeLocation.getLongitude(), 60, 1);

            Chest randomChest;
            for (Point p : randomPoints) {
                randomChest = new Chest(-1L, user, p.getLongitude(), p.getLatitude(), new Date(System.currentTimeMillis()), null, getChestLevel());
                chestRepository.save(randomChest);
            }
            //fixme remove mock for fronts
            Chest c = new Chest(-1L, user, homeLocation.getLongitude() - 0.00024, homeLocation.getLatitude() - 0.00024, new Date(System.currentTimeMillis()), null, getChestLevel());
            chestRepository.save(c);
        }

        //todo change to local variable now go to sleep
        chests = chestRepository.findByUserAndDateCreated(user, new Date(System.currentTimeMillis()));
        return chestMapper.ChestToResponse(chests);
    }

    @GetMapping("/my-statistics")
    UserStatistics getMyStatistics() {
        return userStatisticsService.getUserStatistics(getAuthenticatedUser());
    }

    public long calculateExpFromChest(Chest chest) {
        return chest.getValue() * 10;
    }

    private long getChestLevel() {
        double roll = Math.random();
        //todo fix magic numbers to non-mock
        //0.6 0.9 0.975
        if(roll < 0.25) {
            return 1;
        } else if(roll < 0.5) {
            return 2;
        } else if(roll < 0.75) {
            return 3;
        } else {
            return 4;
        }
    }

    @PostMapping("/open-chest/{id}")
    private OpenedChest openChest(@PathVariable(name = "id") Long id) {
        Optional<Chest> chest = chestRepository.findById(id);
        chest.get().setDateFound(new Timestamp(System.currentTimeMillis()));
        chestRepository.save(chest.get()); //todo make sure that chest can only by opened once

        ApplicationUser user = getAuthenticatedUser();

        //todo move generating exp somewhere else?
        Long gainedExp = calculateExpFromChest(chest.get());
        user.setExperience(user.getExperience() + gainedExp);
        user.setLevel(userStatisticsService.getLevelFromExp(user.getExperience()));

        applicationUserRepository.save(user);

        OpenedChest response = new OpenedChest();
        response.setExpGained(gainedExp);

        return response;
    }

    //todo change mappings to actually rest wihout get- set-
    @GetMapping("/get-home")
    String getHomeLocation() throws HomeLocationNotSetException {
        ApplicationUser user = (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<HomeLocation> homeLocation = Optional.ofNullable(homeLocationRepository.findFirstByUserOrderByDateAddedDesc(user));

        if(!homeLocation.isPresent()) throw new HomeLocationNotSetException();

        //todo god why
        return "{\n\"longitude\":\"" + homeLocation.get().getLongitude() + "\",\n\"latitude\":\"" + homeLocation.get().getLatitude() + "\"\n}";
    }

    //todo not much security here
    @PostMapping("/avatar")
    public String avatarUpload(@RequestParam("file") MultipartFile file) throws IOException {
        ApplicationUser user = getAuthenticatedUser();
        File newFile = new File("./avatars/" + user.getUsername() + ".png");
        newFile.getParentFile().mkdirs();
        FileCopyUtils.copy(file.getBytes(), newFile);
        return "success";
    }

    @GetMapping(
            value =  "/avatar",
            produces = MediaType.IMAGE_PNG_VALUE
    )
    public ResponseEntity<Resource> avatarDownload() throws MalformedURLException, AvatarNotSetException {
        ApplicationUser user = getAuthenticatedUser();

        Path filePath = Paths.get("./avatars/" + user.getUsername() + ".png");
        Resource resource = new UrlResource(filePath.toUri());
        if(resource.exists()) {
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);
        } else {
            throw new AvatarNotSetException();
        }
    }
}


