package pl.mateusz.example.friendoo.user.location;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@SuppressWarnings("checkstyle:MissingJavadocType")
@RestController
@RequestMapping("/api/locations")
public class UserLocationController {

  private final UserLocationService userLocationService;

  public UserLocationController(UserLocationService userLocationService) {
    this.userLocationService = userLocationService;
  }

  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  @GetMapping("/search")
  public ResponseEntity<List<String>> searchLocationByCityName(@RequestParam(name = "query")
                                                                 String query) {
    List<String> locations = userLocationService.getLocations(query);
    return ResponseEntity.ok(locations);
  }


}
