package com.gateau.preto.cake.orderer.authentication.infraestructure;

public class UserAlreadyExistsException extends HandledException {
  public UserAlreadyExistsException() {
    super("Este email ja está cadastrado!");
  }
}
