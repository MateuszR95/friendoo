package pl.mateusz.example.friendoo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web configuration class that registers the LowercaseUrlInterceptor.
 * This interceptor converts all URLs to lowercase.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

  private final LowercaseUrlInterceptor lowercaseUrlInterceptor;

  public WebConfig(LowercaseUrlInterceptor lowercaseUrlInterceptor) {
    this.lowercaseUrlInterceptor = lowercaseUrlInterceptor;
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(lowercaseUrlInterceptor)
        .addPathPatterns("/**");
  }
}
