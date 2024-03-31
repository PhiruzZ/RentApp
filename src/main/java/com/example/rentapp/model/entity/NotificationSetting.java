package com.example.rentapp.model.entity;

import com.example.rentapp.model.enums.NotificationChannel;
import com.example.rentapp.model.request.CreateNotificationSettingRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "notification_setting")
public class NotificationSetting extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    @Column(name = "notification_channel")
    private NotificationChannel notificationChannel;

    public static NotificationSetting of(CreateNotificationSettingRequest request, UserEntity user) {
        NotificationSetting setting = new NotificationSetting();
        setting.setUser(user);
        setting.setNotificationChannel(request.getNotificationChannel());
        return setting;
    }
}
