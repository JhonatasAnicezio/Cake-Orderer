package com.gateau.preto.cake.orderer.authentication.application.mapper;

import com.gateau.preto.cake.orderer.authentication.application.dto.RequestCreateUserDto;
import com.gateau.preto.cake.orderer.authentication.application.dto.UserResponseDto;
import com.gateau.preto.cake.orderer.authentication.domain.model.Role;
import com.gateau.preto.cake.orderer.authentication.domain.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("Test class UserMapper")
class UserMapperTest {

  @Autowired
  private UserMapper userMapper;

  @Test
  @DisplayName("Test mapped dto to entity")
  void shouldMapRequestCreateUserDtoToUserEntity() {
    RequestCreateUserDto dto = new RequestCreateUserDto();
    dto.setName("Jhonatas");
    dto.setEmail("jhonatas@email.com");
    dto.setPassword("securePassword");

    User user = userMapper.toEntity(dto);

    assertThat(user).isNotNull();
    assertThat(user.getId()).isNull();
    assertThat(user.getName()).isEqualTo(dto.getName());
    assertThat(user.getEmail()).isEqualTo(dto.getEmail());
    assertThat(user.getPassword()).isEqualTo(dto.getPassword());
  }

  @Test
  @DisplayName("Test mapped entity to dto")
  void shouldMapUserEntityToUserResponseDto() {
    User user = new User();
    user.setId(1L);
    user.setName("Jhonatas");
    user.setEmail("jhonatas@email.com");
    user.setRole(Role.CLIENT);

    UserResponseDto dto = userMapper.fromEntity(user);

    assertThat(dto).isNotNull();
    assertThat(dto.getId()).isEqualTo(user.getId());
    assertThat(dto.getName()).isEqualTo(user.getName());
    assertThat(dto.getEmail()).isEqualTo(user.getEmail());
    assertThat(dto.getRole()).isEqualTo(user.getRole());
  }
}

