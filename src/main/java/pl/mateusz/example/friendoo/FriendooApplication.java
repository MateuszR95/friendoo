package pl.mateusz.example.friendoo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main class for Spring Boot application.
 */
@SpringBootApplication
@EnableScheduling
public class FriendooApplication {
  public static void main(String[] args) {
    SpringApplication.run(FriendooApplication.class, args);
  }

}
