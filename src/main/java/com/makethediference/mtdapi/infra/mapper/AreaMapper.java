package com.makethediference.mtdapi.infra.mapper;

import com.makethediference.mtdapi.domain.dto.area.ListArea;
import com.makethediference.mtdapi.domain.dto.area.RegisterArea;
import com.makethediference.mtdapi.domain.entity.Area;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class AreaMapper {

    public Area toEntity(RegisterArea dto) {
        return new Area(
                null,
                dto.name(),
                dto.color(),
                Collections.emptyList(),
                null
        );
    }

    public ListArea toDto(Area area) {
        return new ListArea(
                area.getAreaId(),
                area.getName(),
                area.getColor()
        );
    }
}
