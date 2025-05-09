package com.gateau.preto.cake.orderer.authentication.domain.repository;

import com.gateau.preto.cake.orderer.authentication.domain.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmail(String userEmail);
}
