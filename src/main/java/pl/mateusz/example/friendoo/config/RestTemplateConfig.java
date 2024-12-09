package pl.mateusz.example.friendoo.config;

import lombok.Getter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@SuppressWarnings("checkstyle:MissingJavadocType")
@Getter
@Configuration
public class RestTemplateConfig {
  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

}