package pl.edu.agh.geoxplore.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.agh.geoxplore.exception.application.AvatarNotSetException;
import pl.edu.agh.geoxplore.exception.application.UserDoesNotExistException;

import java.io.IOException;

public interface IAvatarService {
    Resource getAvatarByUsername(String username) throws AvatarNotSetException, UserDoesNotExistException;

    void saveCurrentUserAvatar(MultipartFile file) throws IOException;
}
