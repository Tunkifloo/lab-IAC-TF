package com.makethediference.mtdapi.infra.repository;

import com.makethediference.mtdapi.domain.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
