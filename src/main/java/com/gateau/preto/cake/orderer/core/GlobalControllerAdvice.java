package com.gateau.preto.cake.orderer.core;

import com.gateau.preto.cake.orderer.authentication.infraestructure.exception.UserAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalControllerAdvice {

  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity<String> handledUserAlreadyExistsException(UserAlreadyExistsException ex) {
    System.out.println(ex.getMessage());
    return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
  }
}
