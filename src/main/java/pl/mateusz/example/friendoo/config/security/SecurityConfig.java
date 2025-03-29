package pl.mateusz.example.friendoo.config.security;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Security configuration class.
 */
@Configuration
public class SecurityConfig {

  /**
   * Configures the security filter chain for the application.
   *
   * @param http the {@link HttpSecurity} to modify
   * @return the {@link SecurityFilterChain} that defines the security configuration
   * @throws Exception if an error occurs while configuring the security filter chain
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(requests -> requests
        .requestMatchers("/", "/img/**", "/styles/**", "/js/**", "/registration",
          "/password-reset-email", "/password-reset", "/save-details").permitAll()
        .requestMatchers("/home/**", "/complete-profile").authenticated()
        .requestMatchers("/activation", "/api/**").permitAll()
        .requestMatchers(PathRequest.toH2Console()).permitAll()
    );
    http.formLogin(login -> login.loginPage("/")
        .usernameParameter("email")
        .passwordParameter("password")
        .permitAll()
        .defaultSuccessUrl("/home")

    );

    http.logout(logout -> logout
        .logoutRequestMatcher(new AntPathRequestMatcher("/logout/**", HttpMethod.POST.name()))
        .logoutSuccessUrl("/").permitAll()
    );

    http.csrf(AbstractHttpConfigurer::disable);
    http.csrf(csrf -> csrf.ignoringRequestMatchers(PathRequest.toH2Console()));
    http.headers(
        config -> config.frameOptions(
          HeadersConfigurer.FrameOptionsConfig::sameOrigin
      )
    );
    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }


}
