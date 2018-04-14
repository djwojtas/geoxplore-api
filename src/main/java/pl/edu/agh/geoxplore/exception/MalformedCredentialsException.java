package pl.edu.agh.geoxplore.exception;

import org.springframework.security.core.AuthenticationException;

public class MalformedCredentialsException extends AuthenticationException {
    public MalformedCredentialsException(String msg) {
        super(msg);
    }
}
