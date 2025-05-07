package com.gateau.preto.cake.orderer.core.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class HeaderUtils {

  public Optional<String> extractToken(HttpServletRequest request) {
    String authHeader = request.getHeader("Authorization");

    if (authHeader == null) {
      return Optional.empty();
    }

    return Optional.of(authHeader.replace("Bearer ", ""));
  }
}
