package com.gateau.preto.cake.orderer.core.security;

import com.gateau.preto.cake.orderer.core.utils.HeaderUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
@DisplayName("Test filter JwtFilter")
public class JwtFilterTest {

  @Autowired
  private HeaderUtils headerUtils;

  @Test
  @DisplayName("Test method extract token success!")
  public void testExtractToken() {
    HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

    Mockito.when(request.getHeader("Authorization"))
        .thenReturn("Bearer tokensupersecret");

    Optional<String> extractedToken = headerUtils.extractToken(request);

    Assertions.assertEquals(Optional.of("tokensupersecret"), extractedToken);
    Mockito.verify(request).getHeader("Authorization");
  }

  @Test
  @DisplayName("Test method extract token fail - authorization not found!")
  public void testExtractTokenAuthorizationNotFound() {
    HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

    Mockito.when(request.getHeader("Authorization"))
        .thenReturn(null);

    Optional<String> extractedToken = headerUtils.extractToken(request);

    Assertions.assertEquals(Optional.empty(), extractedToken);
    Mockito.verify(request).getHeader("Authorization");
  }
}
