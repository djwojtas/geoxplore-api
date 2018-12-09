package pl.edu.agh.geoxplore.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import pl.edu.agh.geoxplore.security.SecurityConstants;

@SpringBootApplication(scanBasePackages={"pl.edu.agh.geoxplore"})
@EnableJpaRepositories("pl.edu.agh.geoxplore.repository")
@EntityScan(basePackages="pl.edu.agh.geoxplore.entity")
public class AppConfig {
    public static void main(String[] args) {
        SpringApplication.run(AppConfig.class, args);
    }

    @Bean
    AmazonS3 amazonS3() {
        return AmazonS3ClientBuilder.standard()
                .withRegion(Regions.EU_CENTRAL_1)
                .withCredentials(new AWSStaticCredentialsProvider(SecurityConstants.awsCredentials))
                .build();
    }
}
