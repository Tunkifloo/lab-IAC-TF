package com.makethediference.mtdapi.web.controller;

import com.makethediference.mtdapi.domain.dto.playlist.ListPlaylist;
import com.makethediference.mtdapi.domain.dto.playlist.RegisterPlaylist;
import com.makethediference.mtdapi.domain.dto.playlist.UpdatePlaylist;
import com.makethediference.mtdapi.service.PlaylistService;
import com.makethediference.mtdapi.service.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/playlists")
@RequiredArgsConstructor
public class PlaylistController {
    private final PlaylistService playlistService;
    private final AuthService authService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")
    @Transactional
    public ResponseEntity<ListPlaylist> registerPlaylist(@RequestBody @Valid RegisterPlaylist registerPlaylistDto) {
        authService.authorizeRegisterPlaylist();

        ListPlaylist savedDto = playlistService.registerPlaylist(registerPlaylistDto);
        return ResponseEntity.ok(savedDto);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllPlaylists() {
        List<ListPlaylist> playlists = playlistService.getPlaylists();

        if (playlists.isEmpty()) {
            // Devuelves JSON con una clave "message"
            Map<String, String> body = new HashMap<>();
            body.put("message", "No se encontraron playlists habilitadas en la BD");
            return ResponseEntity.ok(body);
        }
        return ResponseEntity.ok(playlists);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    @Transactional
    public ResponseEntity<ListPlaylist> updatePlaylist(
            @PathVariable Long id,
            @RequestBody @Valid UpdatePlaylist updatePlaylistDto
    ) {
        ListPlaylist updatedDto = playlistService.updatePlaylist(id, updatePlaylistDto);
        return ResponseEntity.ok(updatedDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/disable/{id}")
    @Transactional
    public ResponseEntity<ListPlaylist> disablePlaylist(@PathVariable Long id) {
        authService.authorizeDisablePlaylist();
        ListPlaylist disabledDto = playlistService.disablePlaylist(id);
        return ResponseEntity.ok(disabledDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/enable/{id}")
    @Transactional
    public ResponseEntity<ListPlaylist> enablePlaylist(@PathVariable Long id) {
        authService.authorizeEnablePlaylist();
        ListPlaylist enabledDto = playlistService.enablePlaylist(id);
        return ResponseEntity.ok(enabledDto);
    }

    @GetMapping("/disabled")
    public ResponseEntity<?> getDisabledPlaylists() {
        List<ListPlaylist> disabled = playlistService.getDisabledPlaylists();
        if (disabled.isEmpty()) {
            return ResponseEntity.ok("No se encontraron playlists deshabilitadas en la BD");
        } else {
            return ResponseEntity.ok(disabled);
        }
    }
}
