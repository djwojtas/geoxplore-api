package pl.edu.agh.geoxplore.config.interceptor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.edu.agh.geoxplore.exception.application.FriendExistsException;
import pl.edu.agh.geoxplore.exception.application.HomeLocationNotSetException;
import pl.edu.agh.geoxplore.exception.application.MalformedRequestException;
import pl.edu.agh.geoxplore.exception.application.UserExistsException;
import pl.edu.agh.geoxplore.exception.error.ApplicationError;
import pl.edu.agh.geoxplore.message.ErrorResponse;

@ControllerAdvice
public class ApplicationInterceptor {
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

    @ExceptionHandler({MalformedRequestException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<ErrorResponse> handleMalformedRequestException() {
        return new ResponseEntity<>(
                new ErrorResponse("Malformed request, please check your json", ApplicationError.MALFORMED_REQUEST.getErrorCode()),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
