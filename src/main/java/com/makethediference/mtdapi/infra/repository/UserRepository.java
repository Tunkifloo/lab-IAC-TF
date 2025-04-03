package com.makethediference.mtdapi.infra.repository;

import com.makethediference.mtdapi.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String admin);
    Optional<User> findByPhoneNumber(String phoneNumber);
    boolean existsByDni(String dni);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByUsername(String username);
}
