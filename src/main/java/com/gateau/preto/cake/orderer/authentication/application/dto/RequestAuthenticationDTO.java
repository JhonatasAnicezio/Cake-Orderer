package com.gateau.preto.cake.orderer.authentication.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestAuthenticationDTO {
  @NotBlank(message = "Email is required!")
  private String email;

  @NotBlank(message = "Password is required!")
  private String password;
}
