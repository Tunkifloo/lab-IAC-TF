package com.makethediference.mtdapi.service.impl;

import com.makethediference.mtdapi.infra.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserNameGeneratorServiceImpl {

    private final UserRepository userRepository;

    public String generateUsername(String name, String paternalSurname, String maternalSurname) {
        String base = buildUsernameBase(name, paternalSurname, maternalSurname);
        int suffix = 1;
        String candidate;
        do {
            candidate = base + suffix;
            if (!userRepository.existsByUsername(candidate)) {
                return candidate;
            }
            suffix++;
        } while (true);
    }

    private String buildUsernameBase(String name, String paternalSurname, String maternalSurname) {
        if (name == null || name.isBlank()) {
            name = "X";
        }
        if (paternalSurname == null || paternalSurname.isBlank()) {
            paternalSurname = "Apellido";
        }
        if (maternalSurname == null || maternalSurname.isBlank()) {
            maternalSurname = "Apellido";
        }
        // Tomar la primera palabra de cada apellido
        String[] paternoPartes = paternalSurname.trim().split("\\s+");
        String primerPaterno = paternoPartes[0];
        String[] maternoPartes = maternalSurname.trim().split("\\s+");
        String primerMaterno = maternoPartes[0];
        // Inicial del nombre y del primer apellido materno
        String inicialNombre = name.substring(0, 1);
        String inicialMaterno = primerMaterno.substring(0, 1);
        return (inicialNombre + primerPaterno + inicialMaterno).toLowerCase();
    }


}

