package com.makethediference.mtdapi.service;

import com.makethediference.mtdapi.domain.entity.FileSector;
import com.makethediference.mtdapi.domain.entity.LandingFiles;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface LandingFilesService {
    LandingFiles saveLandingFile(MultipartFile file, Long adminId, FileSector fileSector, String makerName, String description, String teamName, String stand);
    Optional<LandingFiles> getLandingFileById(Long id);
    List<LandingFiles> getAllLandingFiles();
    Optional<LandingFiles> updateLandingFile(Long id, MultipartFile file);
    boolean disableLandingFile(Long id);
}
