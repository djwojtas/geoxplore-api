package pl.edu.agh.geoxplore.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.agh.geoxplore.exception.application.AvatarNotSetException;
import pl.edu.agh.geoxplore.exception.application.UserDoesNotExistException;
import pl.edu.agh.geoxplore.repository.ApplicationUserRepository;
import pl.edu.agh.geoxplore.security.SecurityConstants;
import pl.edu.agh.geoxplore.service.IAuthenticationService;
import pl.edu.agh.geoxplore.service.IAvatarService;

import java.io.IOException;

@Service
public class AvatarService implements IAvatarService {

    @Autowired
    ApplicationUserRepository applicationUserRepository;

    @Autowired
    IAuthenticationService authenticationService;

    @Autowired
    AmazonS3 s3Client;

    @Override
    public Resource getAvatarByUsername(String username) throws AvatarNotSetException, UserDoesNotExistException {
        String filePath = String.format("avatars/%s.png", username);
        if (applicationUserRepository.findByUsername(username) != null) {
            if (s3Client.doesObjectExist(SecurityConstants.AWSBucketName, filePath)) {
                S3Object object = s3Client.getObject(new GetObjectRequest(SecurityConstants.AWSBucketName, filePath));
                return new InputStreamResource(object.getObjectContent());
            } else {
                throw new AvatarNotSetException();
            }
        } else throw new UserDoesNotExistException();
    }

    @Override
    public void saveCurrentUserAvatar(MultipartFile file) throws IOException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType("image/png");
        objectMetadata.setContentLength(file.getSize());

        String filePath = String.format("avatars/%s.png", authenticationService.getAuthenticatedUser().getUsername());
        s3Client.putObject(
                SecurityConstants.AWSBucketName,
                filePath,
                file.getInputStream(),
                objectMetadata
        );
    }
}
