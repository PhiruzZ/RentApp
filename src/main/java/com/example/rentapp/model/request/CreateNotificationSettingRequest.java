package com.example.rentapp.model.request;

import com.example.rentapp.model.enums.NotificationChannel;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateNotificationSettingRequest {

    @NotNull(message = "notification channel is mandatory!")
    private NotificationChannel notificationChannel;

}
