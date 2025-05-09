package com.gateau.preto.cake.orderer.authentication.domain.service;

import com.gateau.preto.cake.orderer.authentication.application.dto.RequestAuthenticationDto;
import com.gateau.preto.cake.orderer.authentication.application.dto.TokenDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

@SpringBootTest
@DisplayName("Test class AuthenticationService")
public class AuthenticationServiceTest {
  @Autowired
  private AuthenticationService authenticationService;

  @MockBean
  private AuthenticationManager authenticationManager;

  @Test
  @DisplayName("Test method authentication")
  public void authentication() {
    RequestAuthenticationDto auth = RequestAuthenticationDto.builder()
        .email("xicrinho@email.com")
        .password("12345678")
        .build();

    Authentication authentication = Mockito.mock(Authentication.class);
    Mockito.when(authentication.getName()).thenReturn("xicrinho@email.com");
    Mockito.when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
        .thenReturn(authentication);

    TokenDto tokenDto = authenticationService.authenticate(auth);

    Assertions.assertNotNull(tokenDto);
  }

  @Test
  @DisplayName("Test method authentication - case fail")
  public void authenticationFail() {
    RequestAuthenticationDto auth = RequestAuthenticationDto.builder()
        .email("xicrinho@email.com")
        .password("12345678")
        .build();

    Mockito.when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
        .thenThrow(new BadCredentialsException("Bad credentials"));

    Assertions.assertThrows(BadCredentialsException.class, () -> authenticationService.authenticate(auth));
  }
}
