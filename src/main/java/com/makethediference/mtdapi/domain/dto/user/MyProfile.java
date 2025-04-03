package com.makethediference.mtdapi.domain.dto.user;

import com.makethediference.mtdapi.domain.entity.EstimatedHours;
import com.makethediference.mtdapi.domain.entity.Role;

import java.time.LocalDate;

public record MyProfile(
        String username,
        Role role,
        String name,
        String paternalSurname,
        String maternalSurname,
        String dni,
        String email,
        int age,
        LocalDate birthdate,
        String phoneNumber,
        String codeNumber,
        String country,
        String region,
        String motivation,
        EstimatedHours estimatedHours
) {
}
