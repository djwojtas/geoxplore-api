package pl.edu.agh.geoxplore.security.filters;

import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import pl.edu.agh.geoxplore.entity.ApplicationUser;
import pl.edu.agh.geoxplore.security.SecurityConstants;
import pl.edu.agh.geoxplore.service.CustomUserDetailsService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
    private final CustomUserDetailsService customUserDetailsService;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, CustomUserDetailsService customUserDetailsService) {
        super(authenticationManager);
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(SecurityConstants.HEADER_STRING);
        if(header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }
        UsernamePasswordAuthenticationToken usernamePasswordAuthentication = getAuthenticationToken(request);
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthentication);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthenticationToken(HttpServletRequest request) {
        String token = request.getHeader(SecurityConstants.HEADER_STRING);
        if(token == null) return null;
        String username = Jwts.parser().setSigningKey(SecurityConstants.SECRET)
                .parseClaimsJws(token.replace(SecurityConstants.TOKEN_PREFIX, ""))
                .getBody()
                .getSubject();

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
        ApplicationUser applicationUser = customUserDetailsService.loadUserByUsername(username).getApplicationUser();
        return username != null ? new UsernamePasswordAuthenticationToken(applicationUser, null, userDetails.getAuthorities()) : null;
    }
}
