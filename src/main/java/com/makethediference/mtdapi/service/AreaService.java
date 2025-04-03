package com.makethediference.mtdapi.service;

import com.makethediference.mtdapi.domain.dto.area.ListArea;
import com.makethediference.mtdapi.domain.dto.area.RegisterArea;
import com.makethediference.mtdapi.domain.entity.Area;

import java.util.List;

public interface AreaService {
    Area registerArea(RegisterArea dto);
    List<ListArea> getAllAreas();
    ListArea getMyAssignedArea();
    List<ListArea> getPublicAreas();
}
