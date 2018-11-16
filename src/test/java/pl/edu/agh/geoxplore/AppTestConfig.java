package pl.edu.agh.geoxplore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Properties;

@SpringBootApplication(scanBasePackages={"pl.edu.agh.geoxplore"})
@EnableJpaRepositories("pl.edu.agh.geoxplore.repository")
@EntityScan(basePackages="pl.edu.agh.geoxplore.entity")
public class AppTestConfig {
    public static void main(String[] args) { //todo add securityConstants service
        SpringApplication application = new SpringApplication(AppTestConfig.class);
        application.run(args);
    }
}
