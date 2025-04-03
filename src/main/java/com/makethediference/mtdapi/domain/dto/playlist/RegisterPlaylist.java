package com.makethediference.mtdapi.domain.dto.playlist;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record RegisterPlaylist(
        @NotBlank @Length(max = 100) String title,
        @NotBlank @Length(max = 100) String embedUrl,
        @NotBlank @Length(max = 100) String directUrl

) {
}
