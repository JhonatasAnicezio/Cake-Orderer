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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@SpringBootTest
@DisplayName("Test class UserService")
public class UserServiceTest {

  @Autowired
  private UserService userService;

  @MockBean
  private UserRepository userRepository;

  @MockBean
  private PasswordEncoder passwordEncoder;

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

  @Test
  @DisplayName("Test method createUser")
  public void createUserTest() {
    User user = User.builder()
        .email("xicrinho@email.com")
        .password("123456789")
        .role("USER")
        .build();

    Mockito.when(passwordEncoder.encode(Mockito.anyString()))
            .thenReturn("senhasupercriptografada");

    Mockito.when(userRepository.save(Mockito.any(User.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    User newUser = userService.createUser(user);

    Assertions.assertEquals(user.getEmail(), newUser.getEmail());
    Assertions.assertEquals(user.getAuthorities(), newUser.getAuthorities());
    Assertions.assertEquals("senhasupercriptografada", newUser.getPassword());

    Mockito.verify(userRepository).save(Mockito.any(User.class));
    Mockito.verify(passwordEncoder).encode(Mockito.anyString());
  }
}
