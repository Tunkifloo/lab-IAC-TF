package com.makethediference.mtdapi.domain.dto.user;

public record UpdateProfileResponse(
        UpdateProfile updatedProfile,
        String newToken
) {
}
