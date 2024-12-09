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
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import pl.mateusz.example.friendoo.exceptions.AccountNotActivatedException;
import pl.mateusz.example.friendoo.user.role.Role;
import pl.mateusz.example.friendoo.user.role.UserRole;

@SuppressWarnings("checkstyle:MissingJavadocType")
@Configuration
public class SecurityConfig {

  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(requests -> requests
        .requestMatchers("/", "/img/**", "/styles/**", "/js/**", "/registration").permitAll()
        .requestMatchers("/home/**").authenticated()
        .requestMatchers("/activation").permitAll()
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
