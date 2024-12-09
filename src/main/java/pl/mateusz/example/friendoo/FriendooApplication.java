package pl.mateusz.example.friendoo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SuppressWarnings("checkstyle:MissingJavadocType")
@SpringBootApplication
@EnableScheduling
public class FriendooApplication {

    @SuppressWarnings("checkstyle:Indentation")
    public static void main(String[] args) {
      SpringApplication.run(FriendooApplication.class, args);
    }

}
