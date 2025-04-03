package com.makethediference.mtdapi.infra.repository;

import com.makethediference.mtdapi.domain.entity.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaylistRepository extends JpaRepository<Playlist,Long> {
    boolean existsByEmbedUrl(String embedUrl);
    boolean existsByDirectUrl(String directUrl);
    List<Playlist> findAllByEnabledFalse();
    List<Playlist> findAllByEnabledTrue();
}
