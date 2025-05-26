package at.cinemaland;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "at.Cinemaland.Repository")
public class CinemalandApplication {

    public static void main(String[] args) {
        SpringApplication.run(CinemalandApplication.class, args);
    }
}