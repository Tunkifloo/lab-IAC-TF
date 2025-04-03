package com.makethediference.mtdapi.domain.dto.playlist;

public record ListPlaylist(
        Long idPlaylist,
        String title,
        String embedUrl,
        String directUrl,
        boolean enabled
) {
}
