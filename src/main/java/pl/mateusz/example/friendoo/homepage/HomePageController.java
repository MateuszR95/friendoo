package pl.mateusz.example.friendoo.homepage;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for the home page.
 */
@Controller
public class HomePageController {

  @GetMapping("/")
  String homePage() {
    return "index";
  }


}
