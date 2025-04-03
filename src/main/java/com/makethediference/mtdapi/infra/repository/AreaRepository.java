package com.makethediference.mtdapi.infra.repository;

import com.makethediference.mtdapi.domain.entity.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AreaRepository extends JpaRepository<Area, Long> {
    boolean existsByName(String name);
}
