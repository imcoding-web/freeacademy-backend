package fr.imcoding.edu365.config;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;

/**
 * Flyway migration configuration
 *
 * @author mdh
 */
@Configuration
@Profile({"prod", "stage", "preprod1", "preprod2", "aws"})
@ConditionalOnProperty(prefix = "edu365.flyway", name = "delayed-init", havingValue = "true")
public class FlywayConfig {

  /**
   * Override default flyway initializer to do nothing
   */
  @Bean
  public FlywayMigrationInitializer flywayInitializer(Flyway flyway) {
    return new FlywayMigrationInitializer(flyway, (f) -> {
    });
  }

  /**
   * Create a second flyway initializer to run after jpa has created the schema
   */
  @Bean
  @DependsOn("entityManagerFactory")
  public FlywayMigrationInitializer delayedFlywayInitializer(Flyway flyway) {
    return new FlywayMigrationInitializer(flyway, null);
  }
}
