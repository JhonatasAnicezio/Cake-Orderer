package com.gateau.preto.cake.orderer.authentication.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RequestCreateUserDto {
  private String name;
  private String email;
  private String password;
  private String role;
}
