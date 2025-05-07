package com.gateau.preto.cake.orderer.authentication.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayName("Test class JwtServiceTest")
public class JwtServiceTest {

  @Autowired
  private JwtService jwtService;

  @BeforeEach
  void setUp() {
    jwtService = new JwtService("test-secret-key");
  }

  @Test
  @DisplayName("Test methods encode and decode")
  void shouldGenerateAndVerifyTokenCorrectly() {
    String email = "user@example.com";
    String token = jwtService.jwtEncode(email);

    String extractedEmail = jwtService.jwtVerify(token);

    Assertions.assertNotNull(token);
    Assertions.assertEquals(email, extractedEmail);
  }

  @Test
  @DisplayName("Test class with token invalid")
  void shouldThrowExceptionForInvalidToken() {
    String invalidToken = "invalid.token.string";

    Assertions.assertThrows(
        com.auth0.jwt.exceptions.JWTVerificationException.class,
        () -> jwtService.jwtVerify(invalidToken)
    );
  }
}