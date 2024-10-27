package pl.mateusz.example.friendoo.user;

import java.util.Optional;
import org.springframework.stereotype.Service;


@SuppressWarnings("checkstyle:MissingJavadocType")
@Service
public class UserService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;

  }

  public Optional<UserCredentialsDto> findCredentialsByEmail(String email) {
    return userRepository.findUserByEmail(email)
      .map(UserCredentialsDtoMapper::mapToUserCredentialsDto);
  }
}
