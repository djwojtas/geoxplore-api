package pl.edu.agh.geoxplore.exception;

import pl.edu.agh.geoxplore.exception.error.ApplicationError;

public class UserExistsException extends ApplicationException {
    public UserExistsException() {}
    public UserExistsException(String message, ApplicationError error) {
        super(message, error);
    }
}
