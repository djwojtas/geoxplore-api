package pl.edu.agh.geoxplore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse extends DefaultResponse {
    private Long errorCode;

    public ErrorResponse(String message, Long errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
