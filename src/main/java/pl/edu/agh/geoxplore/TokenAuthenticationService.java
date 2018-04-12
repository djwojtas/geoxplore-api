package pl.edu.agh.geoxplore;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface TokenAuthenticationService {
    Authentication getAuthenticationForLogin(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException;
    Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException;
    User addAuthentication(HttpServletResponse response, UserAuthentication authResult);
}