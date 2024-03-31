package com.example.rentapp.repository;

import com.example.rentapp.model.entity.NotificationSetting;
import com.example.rentapp.model.enums.DbStatus;
import com.example.rentapp.model.enums.NotificationChannel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationSettingRepository extends JpaRepository<NotificationSetting, Long> {

    Optional<NotificationSetting> findByUserIdAndNotificationChannelAndDbStatus(Long userId, NotificationChannel channel, DbStatus dbStatus);

    Optional<NotificationSetting> findByIdAndUserIdAndDbStatus(Long id, Long userId, DbStatus dbStatus);

    List<NotificationSetting> findByUserIdAndDbStatus(Long userId, DbStatus dbStatus);

}
