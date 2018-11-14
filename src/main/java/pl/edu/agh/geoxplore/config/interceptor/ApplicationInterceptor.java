package pl.edu.agh.geoxplore.config.interceptor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.edu.agh.geoxplore.exception.application.*;
import pl.edu.agh.geoxplore.exception.error.ApplicationError;
import pl.edu.agh.geoxplore.message.ErrorResponse;

@ControllerAdvice
public class ApplicationInterceptor {
    @ExceptionHandler(AvatarNotSetException.class)
    public ResponseEntity<ErrorResponse> handleAvatarNotSetException() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(
                new ErrorResponse("Avatar not set, please upload it first", ApplicationError.AVATAR_NOT_SET.getErrorCode()),
                headers,
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserDoesNotExistException.class)
    public ResponseEntity<ErrorResponse> handleUserDoesNotExistException() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(
                new ErrorResponse("User with given username does not exist", ApplicationError.USER_DOESNT_EXIST.getErrorCode()),
                headers,
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HomeLocationNotSetException.class)
    public ResponseEntity<ErrorResponse> handleHomeLocationNotSetException() {
        return new ResponseEntity<>(
                new ErrorResponse("Home location not set", ApplicationError.HOME_LOCATION_NOT_SET.getErrorCode()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FriendExistsException.class)
    public ResponseEntity<ErrorResponse> handleFriendExistsException() {
        return new ResponseEntity<>(
                new ErrorResponse("Friend already exists", ApplicationError.FRIEND_EXISTS.getErrorCode()),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserExistsException() {
        return new ResponseEntity<>(
                new ErrorResponse("Username already exists", ApplicationError.USERNAME_EXISTS.getErrorCode()),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ChestAlreadyOpenedException.class)
    public ResponseEntity<ErrorResponse> handleChestAlreadyOpenedException() {
        return new ResponseEntity<>(
                new ErrorResponse("Chest is already opened", ApplicationError.CHEST_ALREADY_OPENED.getErrorCode()),
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ChestDoesNotExistException.class)
    public ResponseEntity<ErrorResponse> handleChestDoesNotExistException() {
        return new ResponseEntity<>(
                new ErrorResponse("Chest not found", ApplicationError.CHEST_DOES_NOT_EXISTS.getErrorCode()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({MalformedRequestException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<ErrorResponse> handleMalformedRequestException() {
        return new ResponseEntity<>(
                new ErrorResponse("Malformed request, please check your json", ApplicationError.MALFORMED_REQUEST.getErrorCode()),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
