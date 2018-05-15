package pl.edu.agh.geoxplore.migration;

import org.flywaydb.core.Flyway;

public class MigrationsUpgrade {
    public static void main(String[] args) {
        Flyway flyway = new Flyway();
        flyway.setDataSource(System.getenv("JDBC_DATABASE_URL"),
                System.getenv("JDBC_DATABASE_USERNAME"),
                System.getenv("JDBC_DATABASE_PASSWORD"));
        flyway.migrate();
    }
}
