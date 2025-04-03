package com.makethediference.mtdapi.domain.dto.volunteer;

public record ValidateVolunteer(
        Long userId,
        boolean approved,
        String adminComments
) {
}
