package pl.edu.agh.geoxplore.exception.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public enum ApplicationError {
    //1xxx - user related errors
    USERNAME_EXISTS("1000"),
    VALIDATION_ERROR("1001"),
    //4xxx - various errors
    MALFORMED_REQUEST("4000");

    @Getter @Setter
    private String errorCode;
}
