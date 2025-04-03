package com.makethediference.mtdapi.domain.dto.volunteer;

import com.makethediference.mtdapi.domain.entity.EstimatedHours;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

public record VolunteerForm(
        @NotBlank @Length(max = 25) String name,
        @NotBlank @Length(max = 25)  String paternalSurname,
        @NotBlank @Length(max = 25)  String maternalSurname,
        @NotBlank @Pattern(regexp = "^[0-9]{8}$") String dni,
        @NotBlank @Email @Length(max = 100) String email,
        @Past(message = "La fecha de nacimiento debe ser en el pasado") @NotNull(message = "La fecha de nacimiento no puede estar vacía") LocalDate birthdate,
        @NotBlank @Pattern(regexp = "^[0-9]{9}$") String phoneNumber,
        @NotBlank String codeNumber,
        @NotBlank @Length(max = 50) String country,
        @NotBlank @Length(max = 50) String region,
        @NotBlank @Length(max = 200) String motivation,
        @NotNull EstimatedHours estimatedHours,
        @NotNull Long areaId
) {
}
