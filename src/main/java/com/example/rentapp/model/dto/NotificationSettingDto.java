package com.example.rentapp.model.dto;

import com.example.rentapp.model.entity.NotificationSetting;
import com.example.rentapp.model.enums.NotificationChannel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class NotificationSettingDto {

    private Long id;
    private NotificationChannel channel;

    public static List<NotificationSettingDto> listOf(List<NotificationSetting> notificationSettings) {
       return notificationSettings.stream()
                .map(notificationSetting ->
                        NotificationSettingDto.builder()
                                .id(notificationSetting.getId())
                                .channel(notificationSetting.getNotificationChannel())
                                .build()
                ).toList();
    }
}
