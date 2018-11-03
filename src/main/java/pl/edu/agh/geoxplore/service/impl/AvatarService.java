package pl.edu.agh.geoxplore.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.agh.geoxplore.entity.ApplicationUser;
import pl.edu.agh.geoxplore.exception.application.AvatarNotSetException;
import pl.edu.agh.geoxplore.exception.application.UserDoesntExistsException;
import pl.edu.agh.geoxplore.repository.ApplicationUserRepository;
import pl.edu.agh.geoxplore.service.IAvatarService;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class AvatarService implements IAvatarService {

    @Autowired
    ApplicationUserRepository applicationUserRepository;

    @Autowired
    AuthenticationService authenticationService;

    @Override
    public Resource getAvatarByUsername(String username) throws MalformedURLException, AvatarNotSetException, UserDoesntExistsException {
        if(applicationUserRepository.findByUsername(username) != null) {
            Path filePath = Paths.get("./avatars/" + username + ".png");
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new AvatarNotSetException();
            }
        } else throw new UserDoesntExistsException();
    }

//    public Resource getCurrentUserAvatar() throws MalformedURLException, AvatarNotSetException {
//        ApplicationUser user = authenticationService.getAuthenticatedUser();
//
//        Path filePath = Paths.get("./avatars/" + user.getUsername() + ".png");
//        Resource resource = new UrlResource(filePath.toUri());
//        if(resource.exists()) {
//            return resource;
//        } else {
//            throw new AvatarNotSetException();
//        }
//    }

    //todo not much security here
    @Override
    public void saveCurrentUserAvatar(MultipartFile file) throws IOException {
        ApplicationUser user = authenticationService.getAuthenticatedUser();
        File newFile = new File("./avatars/" + user.getUsername() + ".png");
        newFile.getParentFile().mkdirs();
        FileCopyUtils.copy(file.getBytes(), newFile);
    }
}
