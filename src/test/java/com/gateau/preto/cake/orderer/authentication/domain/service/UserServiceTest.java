package com.gateau.preto.cake.orderer.authentication.domain.service;

import com.gateau.preto.cake.orderer.authentication.application.dto.UserResponseDto;
import com.gateau.preto.cake.orderer.authentication.domain.model.Role;
import com.gateau.preto.cake.orderer.authentication.domain.model.User;
import com.gateau.preto.cake.orderer.authentication.domain.repository.UserRepository;
import com.gateau.preto.cake.orderer.authentication.infraestructure.exception.UserAlreadyExistsException;
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
  @DisplayName("Test method findEmail")
  public void findEmailTest() {
    Mockito.when(userRepository.findByEmail(Mockito.anyString()))
        .thenReturn(Optional.of(User.builder().build()));

    Optional<User> user = userService.findByEmail("xicrinho@email.com");

    Assertions.assertNotNull(user.get());
  }

  @Test
  @DisplayName("Test method findEmail - case not found user")
  public void findEmailTestNotFound() {
    Mockito.when(userRepository.findByEmail(Mockito.anyString()))
        .thenReturn(Optional.empty());

    Optional<User> user = userService.findByEmail("xicrinho@email.com");

    Assertions.assertTrue(user.isEmpty());
  }

  @Test
  @DisplayName("Test method loadUserByUsername")
  public void loadByUsernameTest() {
    User userInRepository = User.builder()
        .email("xicrinho@email.com")
        .password("123456789")
        .role(Role.CLIENT)
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
  public void createUserTest() throws UserAlreadyExistsException {
    User user = User.builder()
        .name("super xicrinho")
        .email("xicrinho@email.com")
        .password("123456789")
        .role(Role.CLIENT)
        .build();

    UserResponseDto expected = UserResponseDto.builder()
        .name(user.getName())
        .email(user.getEmail())
        .role(user.getRole())
        .build();

    Mockito.when(userRepository.findByEmail(Mockito.anyString()))
        .thenReturn(Optional.empty());

    Mockito.when(userRepository.save(Mockito.any(User.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    UserResponseDto newUser = userService.createUser(user);

    Assertions.assertEquals(expected, newUser);

    Mockito.verify(userRepository).save(Mockito.any(User.class));
    Mockito.verify(userRepository).findByEmail(Mockito.anyString());
  }

  @Test
  @DisplayName("Test method create user already exist")
  public void createUserTestEmailAlreadyExist() throws UserAlreadyExistsException {
    User newUser = User.builder().email("xicrinho@email.com").build();

    Mockito.when(userRepository.findByEmail(Mockito.anyString()))
        .thenReturn(Optional.of(User.builder().build()));

    Assertions.assertThrows(UserAlreadyExistsException.class, () ->
        userService.createUser(newUser));

    Mockito.verify(userRepository).findByEmail(Mockito.anyString());
  }
}
