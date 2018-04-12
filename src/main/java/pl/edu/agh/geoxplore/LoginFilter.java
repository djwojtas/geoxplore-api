package pl.edu.agh.geoxplore;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public class LoginFilter extends AbstractAuthenticationProcessingFilter {
    @Autowired
    @Qualifier("tokenAuthenticationServiceImpl")
    private TokenAuthenticationService tokenAuthenticationService;

    public LoginFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);

        tokenAuthenticationService = (TokenAuthenticationService) ApplicationContextProvider.getApplicationContext()
                .getBean("tokenAuthenticationServiceImpl");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException { // NOSONAR
        UserAuthentication auth = (UserAuthentication) tokenAuthenticationService.getAuthenticationForLogin(request,
                response);
        if (!auth.isAuthenticated()) {
            throw new UserAuthenticationException("Auth to FTAPI is Failed.", auth);
        }

        return auth;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            javax.servlet.FilterChain chain, Authentication authResult) throws IOException, ServletException {
        UserToken token = null;
        try {
            UserAuthentication authResultObject = (UserAuthentication) authResult;
            token = tokenAuthenticationService.addAuthentication(response, authResultObject);

            // Add the authentication to the Security context
            SecurityContextHolder.getContext().setAuthentication(authResult);

            HashMap<String, Object> information = new HashMap<>();
            information.put("USER", authResultObject.getUser());
            information.put("AUTH_TYPE", authResultObject.getAuthType());
            information.put("INFO", authResultObject.getInfo());

            ObjectMapper mapper = new ObjectMapper();
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(mapper.writeValueAsString(information));
        } catch (Exception ex) {
            ErrorResponse errorResponse = new ErrorResponse("UNWANTED_EXCEPTION_RELOGIN_NEEDED", 1009L);
            ObjectMapper mapper = new ObjectMapper();
            response.getWriter().write(mapper.writeValueAsString(errorResponse));
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {

        UserAuthenticationException authException = (UserAuthenticationException) failed;
        Info info = authException.getAuthentication().getInfo();

        response.setHeader("X-AUTH-ERR-DESC", info.getCode() + "-" + info.getDesc());

        ObjectMapper mapper = new ObjectMapper();
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(mapper.writeValueAsString(authException.getAuthentication().getInfo()));

        loginFilterLogger.info("Authentication FAIL. " + Util.getInputLogsSimple("ip, username, reason",
                authException.getAuthentication().getIpAddress(), request.getHeader("username"), info));
    }
}
