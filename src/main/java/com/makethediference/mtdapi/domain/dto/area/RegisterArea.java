package com.makethediference.mtdapi.domain.dto.area;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record RegisterArea(
        @NotBlank @Length(max = 50) String name,
        @NotBlank @Length(max = 7) String color
) {
}
