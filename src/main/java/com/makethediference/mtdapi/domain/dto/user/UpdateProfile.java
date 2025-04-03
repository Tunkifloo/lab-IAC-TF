package com.makethediference.mtdapi.domain.dto.user;

import com.makethediference.mtdapi.domain.entity.EstimatedHours;

import java.time.LocalDate;

public record UpdateProfile(
        String name,
        String paternalSurname,
        String maternalSurname,
        String dni,
        String email,
        LocalDate birthdate,
        String phoneNumber,
        String codeNumber,
        String country,
        String region,
        String motivation,
        EstimatedHours estimatedHours
) {
}
