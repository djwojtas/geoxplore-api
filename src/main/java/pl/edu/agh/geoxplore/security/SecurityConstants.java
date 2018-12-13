package pl.edu.agh.geoxplore.security;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;

public class SecurityConstants { //todo change to resources test/prod
    public static final String SECRET = System.getenv("secret") == null ? "d3Rm" : System.getenv("secret"); // d3Rm for tests
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final long EXPIRATION_TIME = 864000000L;
    public static final AWSCredentials awsCredentials = new BasicAWSCredentials(
            System.getenv("AWSAccessKeyId") == null ? "" : System.getenv("AWSAccessKeyId"),
            System.getenv("AWSSecretKey") == null ? "" : System.getenv("AWSSecretKey")
    );
    public static final String AWSBucketName = "geoxplore";
}
