package com.gateau.preto.cake.orderer.core;

import com.gateau.preto.cake.orderer.authentication.domain.JwtService;
import com.gateau.preto.cake.orderer.authentication.domain.User;
import com.gateau.preto.cake.orderer.authentication.domain.UserService;
import com.gateau.preto.cake.orderer.authentication.infraestructure.exception.UserAlreadyExistsException;
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
public class GlobalControllerAdviceTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  @MockBean
  private JwtService jwtService;

  @Test
  public void handledUserAlreadyExistsExceptionTest() throws Exception {
    String body = """
        {
          "name": "Xicrinho",
          "email": "xicrinho@example.com",
          "password": "senhaForte123",
          "role": "CLIENTE"
        }
        """;

    Mockito.when(userService.createUser(Mockito.any(User.class)))
            .thenThrow(new UserAlreadyExistsException());
    Mockito.when(jwtService.jwtVerify(Mockito.anyString()))
            .thenReturn("xicrinho@email.com");
    Mockito.when(userService.loadUserByUsername(Mockito.anyString()))
            .thenReturn(User.builder().role("SUPER_ADMIN").build());

    mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
            .header(HttpHeaders.AUTHORIZATION, "Bearer tokenadmin")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(MockMvcResultMatchers.status().isConflict())
        .andExpect(MockMvcResultMatchers.content().string("Este email ja está cadastrado!"));

    Mockito.verify(userService).createUser(Mockito.any(User.class));
    Mockito.verify(userService).loadUserByUsername(Mockito.anyString());
    Mockito.verify(jwtService).jwtVerify(Mockito.anyString());
  }
}
