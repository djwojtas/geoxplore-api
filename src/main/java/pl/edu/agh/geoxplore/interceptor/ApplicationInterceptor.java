package pl.edu.agh.geoxplore.interceptor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.edu.agh.geoxplore.exception.ApplicationException;
import pl.edu.agh.geoxplore.exception.UserExistsException;
import pl.edu.agh.geoxplore.exception.error.ApplicationError;
import pl.edu.agh.geoxplore.message.ErrorResponse;

@ControllerAdvice
public class ApplicationInterceptor {
    @ExceptionHandler(UserExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserExistsException(ApplicationException e) {
        return new ResponseEntity<>(
                        new ErrorResponse("Username already exists", ApplicationError.USERNAME_EXISTS.getErrorCode()),
                        HttpStatus.CONFLICT);
    }
}
