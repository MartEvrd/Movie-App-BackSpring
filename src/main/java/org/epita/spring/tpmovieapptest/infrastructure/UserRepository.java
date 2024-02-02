package org.epita.spring.tpmovieapptest.infrastructure;

import org.epita.spring.tpmovieapptest.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
}
