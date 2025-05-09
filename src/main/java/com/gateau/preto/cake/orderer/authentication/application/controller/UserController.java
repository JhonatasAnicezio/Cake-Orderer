package com.gateau.preto.cake.orderer.authentication.application.controller;

import com.gateau.preto.cake.orderer.authentication.application.dto.RequestAuthenticationDto;
import com.gateau.preto.cake.orderer.authentication.application.dto.RequestCreateUserDto;
import com.gateau.preto.cake.orderer.authentication.application.dto.TokenDto;
import com.gateau.preto.cake.orderer.authentication.application.dto.UserResponseDto;
import com.gateau.preto.cake.orderer.authentication.application.mapper.UserMapper;
import com.gateau.preto.cake.orderer.authentication.domain.service.UserService;
import com.gateau.preto.cake.orderer.authentication.infraestructure.exception.IncorrectAuthException;
import com.gateau.preto.cake.orderer.authentication.infraestructure.exception.UserAlreadyExistsException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;
  private final UserMapper userMapper;

  @PostMapping
  public ResponseEntity<UserResponseDto> createUser(
      @RequestBody @Valid RequestCreateUserDto requestCreateUserDto
  ) throws UserAlreadyExistsException {

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(userService.createUser(
            userMapper.toEntity(requestCreateUserDto)
        ));
  }

  @PostMapping("/auth")
  public ResponseEntity<TokenDto> auth(
      @RequestBody @Valid RequestAuthenticationDto requestAuthenticationDto
  ) throws IncorrectAuthException {
    return ResponseEntity.status(HttpStatus.OK)
        .body(userService.authentication(requestAuthenticationDto));
  }
}
