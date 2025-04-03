package com.makethediference.mtdapi.domain.dto.schedule;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ListSchedule(
        String idSchedule,
        LocalDate startDate,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String title,
        String affair,
        Duration duration
) {
}
