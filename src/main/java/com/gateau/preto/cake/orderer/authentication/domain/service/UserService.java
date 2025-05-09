package com.gateau.preto.cake.orderer.authentication.domain.service;

import com.gateau.preto.cake.orderer.authentication.application.dto.RequestAuthenticationDto;
import com.gateau.preto.cake.orderer.authentication.application.dto.TokenDto;
import com.gateau.preto.cake.orderer.authentication.application.dto.UserResponseDto;
import com.gateau.preto.cake.orderer.authentication.application.mapper.UserMapper;
import com.gateau.preto.cake.orderer.authentication.domain.model.User;
import com.gateau.preto.cake.orderer.authentication.domain.repository.UserRepository;
import com.gateau.preto.cake.orderer.authentication.infraestructure.exception.IncorrectAuthException;
import com.gateau.preto.cake.orderer.authentication.infraestructure.exception.UserAlreadyExistsException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final JwtService jwtService;

  public UserResponseDto createUser(User newUser) throws UserAlreadyExistsException {
    String userEmail = newUser.getEmail();

    if (findByEmail(userEmail).isEmpty()) {

      newUser.setPassword(
          new BCryptPasswordEncoder().encode(newUser.getPassword()));

      return userMapper.fromEntity(userRepository.save(newUser));
    }

    throw new UserAlreadyExistsException();
  }

  public TokenDto authentication(RequestAuthenticationDto auth) throws IncorrectAuthException {
    UserDetails user = loadUserByUsername(auth.getEmail());
    String password = new BCryptPasswordEncoder().encode(auth.getPassword());

    if (user.getPassword().equals(password)) {
      return TokenDto.builder()
          .token(jwtService.jwtEncode(user.getUsername()))
          .build();
    }

    throw new IncorrectAuthException();
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
