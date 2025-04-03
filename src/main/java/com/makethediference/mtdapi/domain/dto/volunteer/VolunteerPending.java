package com.makethediference.mtdapi.domain.dto.volunteer;

import com.makethediference.mtdapi.domain.entity.EstimatedHours;
import com.makethediference.mtdapi.domain.entity.VolunteerStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record VolunteerPending(
        Long userId,
        String name,
        String paternalSurname,
        String maternalSurname,
        String email,
        String dni,
        String phoneNumber,
        String codeNumber,
        LocalDate birthdate,
        Long areaId,
        String areaName,
        EstimatedHours estimatedHours,
        String motivation,
        VolunteerStatus status,
        LocalDateTime submissionDate
) {
}
