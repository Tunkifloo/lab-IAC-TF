package com.makethediference.mtdapi.service;

import com.makethediference.mtdapi.domain.dto.playlist.ListPlaylist;
import com.makethediference.mtdapi.domain.dto.playlist.RegisterPlaylist;
import com.makethediference.mtdapi.domain.dto.playlist.UpdatePlaylist;

import java.util.List;

public interface PlaylistService {
    ListPlaylist registerPlaylist(RegisterPlaylist registerPlaylist);
    List<ListPlaylist> getPlaylists();
    ListPlaylist updatePlaylist(Long id, UpdatePlaylist updatePlaylistDto);
    ListPlaylist disablePlaylist(Long id);
    ListPlaylist enablePlaylist(Long id);
    List<ListPlaylist> getDisabledPlaylists();
}
