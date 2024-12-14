package pl.mateusz.example.friendoo.homepage;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@SuppressWarnings("checkstyle:MissingJavadocType")
@Controller
public class HomePageController {

  @GetMapping("/")
  String homePage() {
    return "index";
  }


}
