package com.gateau.preto.cake.orderer.authentication.domain;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
  private final Algorithm algorithm;

  public JwtService(@Value("{spring.secret.jwt.key}") String secret) {
    this.algorithm = Algorithm.HMAC256(secret);
  }

  public String jwtEncode(String email) {
    return JWT.create()
        .withSubject(email)
        .withExpiresAt(Instant.now().plus(2, ChronoUnit.DAYS))
        .sign(algorithm);
  }

  public String jwtVerify(String token) {
    return JWT.require(algorithm)
        .build()
        .verify(token)
        .getSubject();
  }
}
