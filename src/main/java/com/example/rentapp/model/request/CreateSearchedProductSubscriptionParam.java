package com.example.rentapp.model.request;

import com.example.rentapp.model.enums.NotificationChannel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class CreateSearchedProductSubscriptionParam {

    @NotNull
    private Double minPrice;
    @NotNull
    private Double maxPrice;
    @NotNull
    private LocalDate availableFrom;
    @NotNull
    private LocalDate availableUntil;

    @NotNull
    private NotificationChannel notificationChannel;
    @NotBlank
    private String notificationTarget;
    @NotNull
    private Long categoryId;

}
