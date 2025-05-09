package com.gateau.preto.cake.orderer.authentication.application.dto;

import com.gateau.preto.cake.orderer.authentication.domain.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
  private Long id;
  private String name;
  private String email;
  private Role role;
}
