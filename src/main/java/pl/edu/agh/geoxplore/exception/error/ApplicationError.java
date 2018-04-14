package pl.edu.agh.geoxplore.exception.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public enum ApplicationError {
    //1xxx - user related errors
    USERNAME_EXISTS("1000"),
    VALIDATION_ERROR("1001");

    @Getter @Setter
    private String errorCode;
}
