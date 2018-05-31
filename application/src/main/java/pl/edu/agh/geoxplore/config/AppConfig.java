package pl.edu.agh.geoxplore.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages={"pl.edu.agh.geoxplore"})
@EnableJpaRepositories("pl.edu.agh.geoxplore.repository")
@EntityScan(basePackages="pl.edu.agh.geoxplore.entity")
public class AppConfig {
    public static void main(String[] args) {
        SpringApplication.run(AppConfig.class, args);
    }
}
