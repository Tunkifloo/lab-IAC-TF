package com.makethediference.mtdapi.service.impl;

import com.makethediference.mtdapi.domain.dto.area.ListArea;
import com.makethediference.mtdapi.domain.dto.area.RegisterArea;
import com.makethediference.mtdapi.domain.entity.Area;
import com.makethediference.mtdapi.domain.entity.User;
import com.makethediference.mtdapi.infra.repository.AreaRepository;
import com.makethediference.mtdapi.infra.mapper.AreaMapper;
import com.makethediference.mtdapi.infra.repository.UserRepository;
import com.makethediference.mtdapi.service.AreaService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AreaServiceImpl implements AreaService {
    private final AreaRepository areaRepository;
    private final UserRepository userRepository;
    private final AreaMapper areaMapper;

    @Override
    public Area registerArea(RegisterArea dto) {
        Area area = areaMapper.toEntity(dto);
        return areaRepository.save(area);
    }

    @Override
    public List<ListArea> getAllAreas() {
        return areaRepository.findAll().stream()
                .map(areaMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ListArea getMyAssignedArea() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User currentUser = userRepository.findByEmail(username)
                .orElseThrow(() -> new IllegalStateException(
                        "No se encontró el usuario con email: " + username));

        Area appliedArea = currentUser.getAppliedArea();
        if (appliedArea == null) {
            throw new IllegalStateException("No tienes un área asignada.");
        }

        return areaMapper.toDto(appliedArea);
    }

    @Override
    public List<ListArea> getPublicAreas() {
        return areaRepository.findAll().stream()
                .map(area -> new ListArea(
                        area.getAreaId(),
                        area.getName(),
                        area.getColor()
                ))
                .collect(Collectors.toList());
    }
}
