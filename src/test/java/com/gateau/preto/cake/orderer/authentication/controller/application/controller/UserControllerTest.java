package com.gateau.preto.cake.orderer.authentication.controller.application.controller;

import com.gateau.preto.cake.orderer.authentication.application.dto.UserResponseDto;
import com.gateau.preto.cake.orderer.authentication.domain.model.Role;
import com.gateau.preto.cake.orderer.authentication.domain.service.JwtService;
import com.gateau.preto.cake.orderer.authentication.domain.model.User;
import com.gateau.preto.cake.orderer.authentication.domain.service.UserService;
import com.gateau.preto.cake.orderer.authentication.infraestructure.exception.UserAlreadyExistsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Test class UserController")
public class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private JwtService jwtService;

  @MockBean
  private UserService userService;

  @Test
  @DisplayName("Test route create user")
  public void createUserTest() throws Exception {
    Mockito.when(jwtService.jwtVerify(Mockito.anyString()))
        .thenReturn("token");
    Mockito.when(userService.loadUserByUsername(Mockito.anyString()))
        .thenReturn(User.builder().role(Role.SUPER_ADMIN).build());
    Mockito.when(userService.createUser(Mockito.any(User.class)))
        .thenReturn(UserResponseDto.builder()
            .id(1L)
            .email("xicrinho@email.com")
            .name("xicrinho")
            .role(Role.SUPER_ADMIN)
            .build());

    mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestCreateUser())
        .header(HttpHeaders.AUTHORIZATION, "Bearer token"))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("xicrinho@email.com"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("xicrinho"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.role").value("SUPER_ADMIN"));
  }

  @Test
  @DisplayName("Test route create user already exists")
  public void createUserAlreadyExistsExceptionTest() throws Exception {
    Mockito.when(userService.createUser(Mockito.any(User.class)))
        .thenThrow(new UserAlreadyExistsException());
    Mockito.when(jwtService.jwtVerify(Mockito.anyString()))
        .thenReturn("xicrinho@email.com");
    Mockito.when(userService.loadUserByUsername(Mockito.anyString()))
        .thenReturn(User.builder().role(Role.SUPER_ADMIN).build());

    mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
            .header(HttpHeaders.AUTHORIZATION, "Bearer tokenadmin")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestCreateUser()))
        .andExpect(MockMvcResultMatchers.status().isConflict())
        .andExpect(MockMvcResultMatchers.content().string("Este email ja está cadastrado!"));

    Mockito.verify(userService).createUser(Mockito.any(User.class));
    Mockito.verify(userService).loadUserByUsername(Mockito.anyString());
    Mockito.verify(jwtService).jwtVerify(Mockito.anyString());
  }

  @Test
  @DisplayName("Test route create user without name")
  public void createUserWithoutName() throws Exception {
    Mockito.when(jwtService.jwtVerify(Mockito.anyString()))
        .thenReturn("xicrinho@email.com");
    Mockito.when(userService.loadUserByUsername(Mockito.anyString()))
        .thenReturn(User.builder().role(Role.SUPER_ADMIN).build());

    mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
            .header(HttpHeaders.AUTHORIZATION, "Bearer tokenadmin")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestCreateUserWithoutName()))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("O nome não pode estar em branco"));

    Mockito.verify(userService).loadUserByUsername(Mockito.anyString());
    Mockito.verify(jwtService).jwtVerify(Mockito.anyString());
  }

  private String requestCreateUser() {
    return """
        {
          "name": "Xicrinho",
          "email": "xicrinho@example.com",
          "password": "senhaForte123",
          "role": "CLIENT"
        }
        """;
  }

  private String requestCreateUserWithoutName() {
    return """
        {
          "email": "xicrinho@example.com",
          "password": "senhaForte123",
          "role": "CLIENT"
        }
        """;
  }

  private String requestCreateUserWithoutEmail() {
    return """
        {
          "name": "Xicrinho",
          "password": "senhaForte123",
          "role": "CLIENT"
        }
        """;
  }

  private String requestCreateUserWithoutPassword() {
    return """
        {
          "name": "Xicrinho",
          "email": "xicrinho@example.com",
          "role": "CLIENT"
        }
        """;
  }

  private String requestCreateUserWithoutRole() {
    return """
        {
          "name": "Xicrinho",
          "email": "xicrinho@example.com",
          "password": "senhaForte123",
        }
        """;
  }
}
