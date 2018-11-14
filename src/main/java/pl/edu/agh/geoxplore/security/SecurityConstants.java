package pl.edu.agh.geoxplore.security;

public class SecurityConstants {
    public static final String SECRET = System.getenv("secret");
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final long EXPIRATION_TIME = 864000000L;
}
