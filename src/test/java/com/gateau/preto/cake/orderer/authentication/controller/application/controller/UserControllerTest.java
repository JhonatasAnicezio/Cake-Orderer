package com.gateau.preto.cake.orderer.authentication.controller.application.controller;

import com.gateau.preto.cake.orderer.authentication.application.dto.RequestAuthenticationDto;
import com.gateau.preto.cake.orderer.authentication.application.dto.TokenDto;
import com.gateau.preto.cake.orderer.authentication.application.dto.UserResponseDto;
import com.gateau.preto.cake.orderer.authentication.domain.model.Role;
import com.gateau.preto.cake.orderer.authentication.domain.service.AuthenticationService;
import com.gateau.preto.cake.orderer.authentication.domain.service.JwtService;
import com.gateau.preto.cake.orderer.authentication.domain.model.User;
import com.gateau.preto.cake.orderer.authentication.domain.service.UserService;
import com.gateau.preto.cake.orderer.authentication.infraestructure.exception.UserAlreadyExistsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
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

  @MockBean
  private AuthenticationService authenticationService;

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

  @Test
  @DisplayName("Test route create user without email")
  public void createUserWithoutEmail() throws Exception {
    Mockito.when(jwtService.jwtVerify(Mockito.anyString()))
        .thenReturn("xicrinho@email.com");
    Mockito.when(userService.loadUserByUsername(Mockito.anyString()))
        .thenReturn(User.builder().role(Role.SUPER_ADMIN).build());

    mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
            .header(HttpHeaders.AUTHORIZATION, "Bearer tokenadmin")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestCreateUserWithoutEmail()))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("O e-mail é obrigatório"));

    Mockito.verify(userService).loadUserByUsername(Mockito.anyString());
    Mockito.verify(jwtService).jwtVerify(Mockito.anyString());
  }

  @Test
  @DisplayName("Test route create user without password")
  public void createUserWithoutPassword() throws Exception {
    Mockito.when(jwtService.jwtVerify(Mockito.anyString()))
        .thenReturn("xicrinho@email.com");
    Mockito.when(userService.loadUserByUsername(Mockito.anyString()))
        .thenReturn(User.builder().role(Role.SUPER_ADMIN).build());

    mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
            .header(HttpHeaders.AUTHORIZATION, "Bearer tokenadmin")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestCreateUserWithoutPassword()))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.password").value("A senha é obrigatória"));

    Mockito.verify(userService).loadUserByUsername(Mockito.anyString());
    Mockito.verify(jwtService).jwtVerify(Mockito.anyString());
  }

  @Test
  @DisplayName("Test route create user without role")
  public void createUserWithoutRole() throws Exception {
    Mockito.when(jwtService.jwtVerify(Mockito.anyString()))
        .thenReturn("xicrinho@email.com");
    Mockito.when(userService.loadUserByUsername(Mockito.anyString()))
        .thenReturn(User.builder().role(Role.SUPER_ADMIN).build());

    mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
            .header(HttpHeaders.AUTHORIZATION, "Bearer tokenadmin")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestCreateUserWithoutRole()))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());

    Mockito.verify(userService).loadUserByUsername(Mockito.anyString());
    Mockito.verify(jwtService).jwtVerify(Mockito.anyString());
  }

  @Test
  @DisplayName("Test create user with short name")
  public void createUserWithShortName() throws Exception {
    Mockito.when(jwtService.jwtVerify(Mockito.anyString()))
        .thenReturn("admin@example.com");
    Mockito.when(userService.loadUserByUsername(Mockito.anyString()))
        .thenReturn(User.builder().role(Role.SUPER_ADMIN).build());

    String payload = """
      {
        "name": "Ana",
        "email": "ana@example.com",
        "password": "senhaForte123",
        "role": "CLIENT"
      }
      """;

    mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
            .header(HttpHeaders.AUTHORIZATION, "Bearer tokenadmin")
            .contentType(MediaType.APPLICATION_JSON)
            .content(payload))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("O nome deve ter no minimo 4"));
  }

  @Test
  @DisplayName("Test create user with invalid email")
  public void createUserWithInvalidEmail() throws Exception {
    Mockito.when(jwtService.jwtVerify(Mockito.anyString()))
        .thenReturn("admin@example.com");
    Mockito.when(userService.loadUserByUsername(Mockito.anyString()))
        .thenReturn(User.builder().role(Role.SUPER_ADMIN).build());

    String payload = """
      {
        "name": "Xicrinho",
        "email": "emailinvalido",
        "password": "senhaForte123",
        "role": "CLIENT"
      }
      """;

    mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
            .header(HttpHeaders.AUTHORIZATION, "Bearer tokenadmin")
            .contentType(MediaType.APPLICATION_JSON)
            .content(payload))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("E-mail inválido"));
  }

  @Test
  @DisplayName("Test create user with short password")
  public void createUserWithShortPassword() throws Exception {
    Mockito.when(jwtService.jwtVerify(Mockito.anyString()))
        .thenReturn("admin@example.com");
    Mockito.when(userService.loadUserByUsername(Mockito.anyString()))
        .thenReturn(User.builder().role(Role.SUPER_ADMIN).build());

    String payload = """
      {
        "name": "Xicrinho",
        "email": "xicrinho@example.com",
        "password": "123",
        "role": "CLIENT"
      }
      """;

    mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
            .header(HttpHeaders.AUTHORIZATION, "Bearer tokenadmin")
            .contentType(MediaType.APPLICATION_JSON)
            .content(payload))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.password").value("A senha deve ter no minimo 6 caracteres"));
  }

  @Test
  @DisplayName("Test create user with invalid role")
  public void createUserWithInvalidRole() throws Exception {
    Mockito.when(jwtService.jwtVerify(Mockito.anyString()))
        .thenReturn("admin@example.com");
    Mockito.when(userService.loadUserByUsername(Mockito.anyString()))
        .thenReturn(User.builder().role(Role.SUPER_ADMIN).build());

    String payload = """
      {
        "name": "Xicrinho",
        "email": "xicrinho@example.com",
        "password": "1231443212",
        "role": "CLIENTE"
      }
      """;

    mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
            .header(HttpHeaders.AUTHORIZATION, "Bearer tokenadmin")
            .contentType(MediaType.APPLICATION_JSON)
            .content(payload))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  @DisplayName("Test method authentication")
  public void authenticationTest() throws Exception {
    Mockito.when(authenticationService.authenticate(Mockito.any(RequestAuthenticationDto.class)))
            .thenReturn(TokenDto.builder().token("tokendousuario").build());

    mockMvc.perform(MockMvcRequestBuilders.post("/api/users/auth")
          .contentType(MediaType.APPLICATION_JSON)
          .content(requestAuthentication()))
        .andExpect(MockMvcResultMatchers.status().isOk());

    Mockito.verify(authenticationService).authenticate(Mockito.any(RequestAuthenticationDto.class));
  }

  @Test
  @DisplayName("Test method authentication - fail authenticate")
  public void authenticationTestFail() throws Exception {
    Mockito.when(authenticationService.authenticate(Mockito.any(RequestAuthenticationDto.class)))
        .thenThrow(new BadCredentialsException(""));

    mockMvc.perform(MockMvcRequestBuilders.post("/api/users/auth")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestAuthentication()))
        .andExpect(MockMvcResultMatchers.status().isForbidden());

    Mockito.verify(authenticationService).authenticate(Mockito.any(RequestAuthenticationDto.class));
  }

  @Test
  @DisplayName("Deve retornar 400 quando o e-mail estiver em branco")
  void deveRetornar400ComEmailEmBranco() throws Exception {
    String payload = """
      {
        "email": "",
        "password": "senhaForte123"
      }
      """;

    mockMvc.perform(MockMvcRequestBuilders.post("/api/users/auth")
            .contentType(MediaType.APPLICATION_JSON)
            .content(payload))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("Email is required!"));
  }

  @Test
  @DisplayName("Deve retornar 400 quando a senha estiver em branco")
  void deveRetornar400ComSenhaEmBranco() throws Exception {
    String payload = """
      {
        "email": "xicrinho@example.com",
        "password": ""
      }
      """;

    mockMvc.perform(MockMvcRequestBuilders.post("/api/users/auth")
            .contentType(MediaType.APPLICATION_JSON)
            .content(payload))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.password").value("Password is required!"));
  }

  @Test
  @DisplayName("Deve retornar 400 quando email e senha estiverem em branco")
  void deveRetornar400ComTodosCamposEmBranco() throws Exception {
    String payload = """
      {
        "email": "",
        "password": ""
      }
      """;

    mockMvc.perform(MockMvcRequestBuilders.post("/api/users/auth")
            .contentType(MediaType.APPLICATION_JSON)
            .content(payload))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("Email is required!"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.password").value("Password is required!"));
  }


  private String requestAuthentication() {
    return """
        {
          "email": "xicrinho@example.com",
          "password": "senhaForte123"
        }
        """;
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
