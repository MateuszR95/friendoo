package pl.mateusz.example.friendoo.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@SuppressWarnings("checkstyle:MissingJavadocType")
@Configuration
public class SecurityConfig {

  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(requests -> requests
        .requestMatchers("/", "/img/**", "/styles/**", "/js/**", "/fragments/**").permitAll()
        .requestMatchers("/home/**").authenticated()
        .requestMatchers(PathRequest.toH2Console()).permitAll()
    );
    http.formLogin(login -> login.loginPage("/")
        .usernameParameter("email")
        .passwordParameter("password")
        .permitAll().defaultSuccessUrl("/home"));


    http.csrf(csrf -> csrf.disable());
    http.csrf(csrf -> csrf.ignoringRequestMatchers(PathRequest.toH2Console()));
    http.headers(
        config -> config.frameOptions(
          options -> options.sameOrigin()
      )
    );
    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

}
