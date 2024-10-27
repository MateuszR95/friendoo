package pl.mateusz.example.friendoo.homepage;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@SuppressWarnings("checkstyle:MissingJavadocType")
@Controller
public class HomePageController {

  @GetMapping("/")
  String homePage() {
    return "index";
  }


}
