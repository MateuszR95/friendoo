package pl.mateusz.example.friendoo.user;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@SuppressWarnings("checkstyle:MissingJavadocType")
@Controller
public class UserController {

  @PostMapping("/")
  String login() {
    return "redirect:/home";
  }

  @GetMapping("/home")
  String displayUserProfile(Authentication authentication, Model model) {
    model.addAttribute("username", authentication.getName());
    return "news-feed";
  }
}
