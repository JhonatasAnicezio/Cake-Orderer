package com.gateau.preto.cake.orderer.authentication.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
    return userRepository.findByEmail(userEmail)
        .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado!"));
  }
}
