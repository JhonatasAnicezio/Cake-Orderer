package com.gateau.preto.cake.orderer.authentication.application.dto;

import lombok.Data;

@Data
public class UserResponseDTO {
  private Long id;
  private String name;
  private String email;
  private String role;
}
