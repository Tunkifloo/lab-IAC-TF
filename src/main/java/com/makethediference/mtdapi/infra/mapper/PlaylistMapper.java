package com.makethediference.mtdapi.infra.mapper;

import com.makethediference.mtdapi.domain.dto.playlist.ListPlaylist;
import com.makethediference.mtdapi.domain.dto.playlist.RegisterPlaylist;
import com.makethediference.mtdapi.domain.dto.playlist.UpdatePlaylist;
import com.makethediference.mtdapi.domain.entity.Playlist;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlaylistMapper {

    public Playlist toEntity(RegisterPlaylist registerPlaylist) {
        Playlist playlist = new Playlist();

        fillPlaylist(
                playlist,
                registerPlaylist.title(),
                registerPlaylist.embedUrl(),
                registerPlaylist.directUrl()
        );
        return playlist;
    }

    public ListPlaylist toDto(Playlist playlist) {
        return new ListPlaylist(
                playlist.getIdPlaylist(),
                playlist.getTitle(),
                playlist.getEmbedUrl(),
                playlist.getDirectUrl(),
                playlist.isEnabled()
        );
    }

    public void updateFromPlaylist(UpdatePlaylist dto, Playlist playlist) {
        fillPlaylist(
                playlist,
                dto.title(),
                dto.embedUrl(),
                dto.directUrl()
        );
    }

    private void fillPlaylist(Playlist playlist, String title, String embedUrl, String directUrl) {
        playlist.setTitle(title);
        playlist.setEmbedUrl(embedUrl);
        playlist.setDirectUrl(directUrl);
    }
}
