package pl.edu.agh.geoxplore.message;

import lombok.Data;

@Data
public class ErrorResponse extends DefaultResponse{
    private String errorCode;

    public ErrorResponse(String message, String applicationError) {
        super(message);
        this.errorCode = applicationError;
    }
}
