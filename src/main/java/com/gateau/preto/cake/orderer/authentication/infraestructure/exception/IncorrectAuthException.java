package com.gateau.preto.cake.orderer.authentication.infraestructure.exception;

import com.gateau.preto.cake.orderer.core.exception.HandledException;

public class IncorrectAuthException extends HandledException {
  public IncorrectAuthException() {
    super("Email ou senha do usuário inválido!");
  }
}
