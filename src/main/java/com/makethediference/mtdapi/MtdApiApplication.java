package com.makethediference.mtdapi;

import com.makethediference.mtdapi.domain.entity.Admin;
import com.makethediference.mtdapi.domain.entity.EstimatedHours;
import com.makethediference.mtdapi.domain.entity.Role;
import com.makethediference.mtdapi.infra.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@SpringBootApplication
@RequiredArgsConstructor
@EnableCaching
public class MtdApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MtdApiApplication.class, args);
    }

    @Bean
    public CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            boolean existsByEmail = userRepository.findByEmail("admin@admin.com").isPresent();
            boolean existsByUsername = userRepository.findByUsername("admin").isPresent();
            boolean existsByPhoneNumber = userRepository.findByPhoneNumber("123456789").isPresent();

            if (!existsByEmail && !existsByUsername && !existsByPhoneNumber) {
                Admin defaultAdmin = Admin.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin123"))
                        .role(Role.ADMIN)
                        .name("Luis")
                        .paternalSurname("Mostacero")
                        .maternalSurname("Cieza")
                        .birthdate(LocalDate.of(1980, 1, 1))
                        .dni("00000000")
                        .email("admin@admin.com")
                        .phoneNumber("123456789")
                        .codeNumber("+51")
                        .country("Peru")
                        .region("La Libertad")
                        .motivation("Luisda Luisda Luisda Luisda Luisda Luisda")
                        .estimatedHours(EstimatedHours.PLUS_TEN)
                        .enabled(true)
                        .firstLogin(true)
                        .build();

                userRepository.save(defaultAdmin);
            }
        };
    }
}
