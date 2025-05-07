package com.gateau.preto.cake.orderer.authentication.domain;

import com.gateau.preto.cake.orderer.authentication.application.UserMapper;
import com.gateau.preto.cake.orderer.authentication.application.dto.UserResponseDto;
import com.gateau.preto.cake.orderer.authentication.infraestructure.exception.UserAlreadyExistsException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserMapper userMapper;

  public UserResponseDto createUser(User newUser) throws UserAlreadyExistsException {
    String userEmail = newUser.getEmail();

    if (findByEmail(userEmail).isEmpty()) {

      newUser.setPassword(
          passwordEncoder.encode(newUser.getPassword()));

      return userMapper.fromEntity(userRepository.save(newUser));
    }

    throw new UserAlreadyExistsException();
  }

  public Optional<User> findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  @Override
  public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
    return userRepository.findByEmail(userEmail)
        .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado!"));
  }
}
