package pl.edu.agh.geoxplore.exception.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public enum ApplicationError {
    //1xxx - user related errors
    USERNAME_EXISTS("1000"),
    VALIDATION_ERROR("1001"),
    FRIEND_EXISTS("1002"),
    HOME_LOCATION_NOT_SET("1003"),
    AVATAR_NOT_SET("1004"),
    USER_DOESNT_EXIST("1005"),
    CHEST_ALREADY_OPENED("1006"),
    CHEST_DOES_NOT_EXISTS("1007"),
    //4xxx - various errors
    MALFORMED_REQUEST("4000");

    @Getter @Setter
    private String errorCode;
}
