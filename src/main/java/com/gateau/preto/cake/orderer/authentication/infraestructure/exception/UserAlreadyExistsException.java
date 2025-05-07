package com.gateau.preto.cake.orderer.authentication.infraestructure.exception;

import com.gateau.preto.cake.orderer.core.exception.HandledException;

public class UserAlreadyExistsException extends HandledException {
  public UserAlreadyExistsException() {
    super("Este email ja está cadastrado!");
  }
}
