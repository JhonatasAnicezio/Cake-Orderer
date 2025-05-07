package com.gateau.preto.cake.orderer.authentication.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestCreateUserDto {
  @NotBlank(message = "O nome não pode estar em branco")
  @Size(min = 4, max = 20, message = "O nome deve ter no minimo 4")
  private String name;

  @NotBlank(message = "O e-mail é obrigatório")
  @Email(message = "E-mail inválido")
  private String email;

  @NotBlank(message = "A senha é obrigatória")
  @Size(min = 6, message = "A senha deve ter no minimo 6 caracteres")
  private String password;

  @NotBlank(message = "O tipo de usuário (role) é obrigatório")
  private String role;
}
