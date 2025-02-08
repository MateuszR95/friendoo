package pl.mateusz.example.friendoo.contoller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import pl.mateusz.example.friendoo.homepage.HomePageController;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HomePageController.class)
@WithMockUser
public class HomePageControllerTests {

  @Autowired
  private MockMvc mockMvc;

  @Test
  public void shouldReturnHomePageView() throws Exception {
    mockMvc.perform(get("/"))
      .andExpect(status().isOk())
      .andExpect(view().name("index"))
      .andExpect(content().string(containsString("Witaj na Friendoo- zaloguj się lub zarejestruj")));
  }

}
