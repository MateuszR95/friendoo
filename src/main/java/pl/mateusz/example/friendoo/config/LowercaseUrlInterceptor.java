package pl.mateusz.example.friendoo.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Interceptor to convert all URLs to lowercase.
 * If a URL is not lowercase, it redirects to the lowercase version.
 */
@Component
public class LowercaseUrlInterceptor implements HandlerInterceptor {

  /**
   * Pre-handle method to check if the URL is lowercase.
   * If not, it redirects to the lowercase version.
   *
   * @param request  the HTTP request
   * @param response the HTTP response
   * @param handler  the handler
   * @return true if the URL is lowercase, false otherwise
   */
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws IOException {
    String originalUrl = request.getRequestURI();
    String lowercaseUrl = originalUrl.toLowerCase();
    if (!originalUrl.equals(lowercaseUrl)) {
      response.sendRedirect(request.getContextPath() + lowercaseUrl);
      return false;
    }
    return true;
  }
}
