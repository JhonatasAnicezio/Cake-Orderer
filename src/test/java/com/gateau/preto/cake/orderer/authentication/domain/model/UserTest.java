package com.gateau.preto.cake.orderer.authentication.domain.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

@SpringBootTest
@DisplayName("Test class User")
public class UserTest {

  @Test
  @DisplayName("Test methods getter")
  public void testMethodGetPassword() {
    SimpleGrantedAuthority authority = new SimpleGrantedAuthority("CLIENT");
    User user = User.builder()
        .email("xicrinho@email.com")
        .password("123456789")
        .role(Role.CLIENT)
        .build();

    Assertions.assertEquals("123456789", user.getPassword());
    Assertions.assertEquals("xicrinho@email.com", user.getUsername());
    Assertions.assertEquals(List.of(authority), user.getAuthorities());
  }
}
