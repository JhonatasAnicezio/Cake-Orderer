package com.gateau.preto.cake.orderer.authentication.application.mapper;

import com.gateau.preto.cake.orderer.authentication.application.dto.RequestCreateUserDto;
import com.gateau.preto.cake.orderer.authentication.application.dto.UserResponseDto;
import com.gateau.preto.cake.orderer.authentication.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserMapper {

  @Mapping(target = "id", ignore = true)
  User toEntity(RequestCreateUserDto requestCreateUserDto);

  @Mapping(target = "id", source = "id")
  @Mapping(target = "name", source = "name")
  @Mapping(target = "email", source = "email")
  @Mapping(target = "role", source = "role")
  UserResponseDto fromEntity(User user);
}
