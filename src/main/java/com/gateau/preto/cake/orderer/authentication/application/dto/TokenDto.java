package com.gateau.preto.cake.orderer.authentication.application.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenDto {
  private String token;
}
