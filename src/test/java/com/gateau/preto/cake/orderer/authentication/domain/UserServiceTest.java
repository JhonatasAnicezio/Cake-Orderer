package com.gateau.preto.cake.orderer.authentication.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

@SpringBootTest
@DisplayName("Test class UserService")
public class UserServiceTest {

  @Autowired
  private UserService userService;

  @MockBean
  private UserRepository userRepository;

  @Test
  @DisplayName("Test method loadUserByUsername")
  public void loadByUsernameTest() {
    User userInRepository = User.builder()
        .email("xicrinho@email.com")
        .password("123456789")
        .role("USER")
        .build();

    Mockito.when(userRepository.findByEmail(ArgumentMatchers.anyString()))
        .thenReturn(Optional.of(userInRepository));

    UserDetails userDetails = userService.loadUserByUsername("xicrinho@email.com");

    Assertions.assertEquals("xicrinho@email.com", userDetails.getUsername());

    Mockito.verify(userRepository).findByEmail("xicrinho@email.com");
  }

  @Test
  @DisplayName("Test method loadUserByUsername - user does not exist")
  public void loadByUsernameTestFail() {
    Mockito.when(userRepository.findByEmail(ArgumentMatchers.anyString()))
        .thenThrow(new UsernameNotFoundException("Usuário não encontrado!"));

    Assertions.assertThrows(UsernameNotFoundException.class, () ->
        userService.loadUserByUsername("xicrinho@email.com"));

    Mockito.verify(userRepository).findByEmail("xicrinho@email.com");
  }
}
