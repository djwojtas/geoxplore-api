package pl.edu.agh.geoxplore.exception.application;

import lombok.Getter;
import lombok.Setter;
import pl.edu.agh.geoxplore.exception.error.ApplicationError;

public class ApplicationException extends Exception {
    @Getter @Setter
    private ApplicationError error;

    public ApplicationException(String message, ApplicationError error) {
        super(message);
        this.error = error;
    }

    public ApplicationException() {}
}
