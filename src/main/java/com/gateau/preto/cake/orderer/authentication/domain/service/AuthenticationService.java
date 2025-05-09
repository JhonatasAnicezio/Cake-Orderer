package com.gateau.preto.cake.orderer.authentication.domain.service;

import com.gateau.preto.cake.orderer.authentication.application.dto.RequestAuthenticationDto;
import com.gateau.preto.cake.orderer.authentication.application.dto.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthenticationService {
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;

  public TokenDto authenticate(RequestAuthenticationDto auth) {
    UsernamePasswordAuthenticationToken usernamePassword =
        new UsernamePasswordAuthenticationToken(auth.getEmail(), auth.getPassword());

    Authentication user = authenticationManager.authenticate(usernamePassword);

    return TokenDto.builder()
        .token(jwtService.jwtEncode(user.getName()))
        .build();
  }
}
