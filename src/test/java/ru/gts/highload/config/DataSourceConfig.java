package ru.gts.highload.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    /**
     * Создание контейнера на все тесты.
     *
     * @implSpec Если регистрировать контейнер как бин, то он будет пересоздаваться каждый раз, когда в ходе тестов
     * изменяется контекст. Если использовать
     * <a href="https://www.testcontainers.org/test_framework_integration/manual_lifecycle_control/">static-поля</a>,
     * контейнер создаётся только один раз и живёт до тех пор, пока не пройдут все тесты, даже если у них разный Spring
     * Context. Останавливает его по окончании сам testcontainers.
     */
    private static final PostgreSQLContainer<?> PG_CONTAINER;

    static {
        PG_CONTAINER = new PostgreSQLContainer<>("postgres:13");
        PG_CONTAINER
                .withDatabaseName("highload")
                .withUsername("postgres")
                .withPassword("postgres")
                .waitingFor(Wait.forListeningPort())
                .start();
    }

    @Bean
    DataSource getDataSource() {
        PostgreSQLContainer<?> container = PG_CONTAINER;

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:" + container.getFirstMappedPort() + "/" + container.getDatabaseName());
        config.setUsername(container.getUsername());
        config.setPassword(container.getPassword());
        config.setMaximumPoolSize(1);

        return new HikariDataSource(config);
    }

}
