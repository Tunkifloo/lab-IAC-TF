package com.makethediference.mtdapi.domain.dto.notification;

import com.makethediference.mtdapi.domain.entity.NotificationStatus;

public record ListNotification(
        Long idNotification,
        String title,
        String description,
        NotificationStatus status
) {
}
