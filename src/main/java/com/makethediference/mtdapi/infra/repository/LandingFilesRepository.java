package com.makethediference.mtdapi.infra.repository;

import com.makethediference.mtdapi.domain.entity.FileSector;
import com.makethediference.mtdapi.domain.entity.LandingFiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LandingFilesRepository extends JpaRepository<LandingFiles, Long> {
    List<LandingFiles> findByFileTypes(String fileTypes);

    List<LandingFiles> findByAdminUserId(Long adminId);

    long countByFileSector(FileSector fileSector);
}
