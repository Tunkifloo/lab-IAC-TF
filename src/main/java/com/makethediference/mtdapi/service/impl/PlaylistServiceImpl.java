package com.makethediference.mtdapi.service.impl;

import com.makethediference.mtdapi.domain.dto.playlist.ListPlaylist;
import com.makethediference.mtdapi.domain.dto.playlist.RegisterPlaylist;
import com.makethediference.mtdapi.domain.dto.playlist.UpdatePlaylist;
import com.makethediference.mtdapi.domain.entity.Playlist;
import com.makethediference.mtdapi.infra.mapper.PlaylistMapper;
import com.makethediference.mtdapi.infra.repository.PlaylistRepository;
import com.makethediference.mtdapi.service.PlaylistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaylistServiceImpl implements PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final PlaylistMapper playlistMapper;

    @Override
    public ListPlaylist registerPlaylist(RegisterPlaylist registerPlaylistDto) {
        if (playlistRepository.existsByEmbedUrl(registerPlaylistDto.embedUrl())) {
            throw new IllegalArgumentException("Ya existe una  playlist con el path url: " + registerPlaylistDto.embedUrl());
        }
        if (playlistRepository.existsByDirectUrl(registerPlaylistDto.directUrl())) {
            throw new IllegalArgumentException("Ya existe  una  playlist con la url directa: " + registerPlaylistDto.directUrl());
        }
        Playlist entity = playlistMapper.toEntity(registerPlaylistDto);
        Playlist saved = playlistRepository.save(entity);

        return playlistMapper.toDto(saved);
    }

    @Override
    public List<ListPlaylist> getPlaylists() {
        return playlistRepository.findAllByEnabledTrue().stream()
                .map(playlistMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ListPlaylist updatePlaylist(Long id, UpdatePlaylist updatePlaylistDto) {
        Playlist entity = playlistRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No existe la playlist con ID: " + id));

        if (playlistRepository.existsByEmbedUrl(updatePlaylistDto.embedUrl())
                && !entity.getEmbedUrl().equals(updatePlaylistDto.embedUrl())) {
            throw new IllegalArgumentException("Ya existe una playlist con el embedUrl: " + updatePlaylistDto.embedUrl());
        }
        if (playlistRepository.existsByDirectUrl(updatePlaylistDto.directUrl())
                && !entity.getDirectUrl().equals(updatePlaylistDto.directUrl())) {
            throw new IllegalArgumentException("Ya existe una playlist con la url directa: " + updatePlaylistDto.directUrl());
        }

        playlistMapper.updateFromPlaylist(updatePlaylistDto, entity);
        Playlist updated = playlistRepository.save(entity);
        return playlistMapper.toDto(updated);

    }

    @Override
    public ListPlaylist disablePlaylist(Long id) {
        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No existe la playlist con id: " + id));
        playlist.setEnabled(false);
        playlistRepository.save(playlist);
        return playlistMapper.toDto(playlist);
    }

    @Override
    public ListPlaylist enablePlaylist(Long id) {
        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No existe la playlist con id: " + id));
        playlist.setEnabled(true);
        playlistRepository.save(playlist);
        return playlistMapper.toDto(playlist);
    }

    @Override
    public List<ListPlaylist> getDisabledPlaylists() {
        return playlistRepository.findAllByEnabledFalse()
                .stream()
                .map(playlistMapper::toDto)
                .collect(Collectors.toList());
    }
}
