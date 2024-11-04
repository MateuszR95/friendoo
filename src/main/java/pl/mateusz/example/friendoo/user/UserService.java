package pl.mateusz.example.friendoo.user;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
      .map(UserDtoMapper::mapToUserCredentialsDto);
  }

  public Optional<UserDisplayDto> findUserToDisplay(String email) {
    return userRepository.findUserByEmail(email)
      .map(UserDtoMapper::mapToUserDisplayDto);
  }

  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  public List<UserDisplayDto> getUserFriendsList(String email) {
    List<User> friendsByEmail = userRepository.findFriendsByEmail(email);
    return friendsByEmail.stream().map(UserDtoMapper::mapToUserDisplayDto)
      .collect(Collectors.toList());
  }

}
