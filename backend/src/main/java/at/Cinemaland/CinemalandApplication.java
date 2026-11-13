package at.Cinemaland;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Must be in the same package as Controller/, Service/, Model/, Repository/ - otherwise Spring finds none of them
@SpringBootApplication
public class CinemalandApplication {

    public static void main(String[] args) {
        SpringApplication.run(CinemalandApplication.class, args);
    }
}
