package com.example.rentapp.model.dto;

import com.example.rentapp.model.entity.ProductCategory;
import com.example.rentapp.model.entity.SearchedProductSubscription;
import com.example.rentapp.model.enums.NotificationChannel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@Builder
public class SearchedProductSubscriptionDto {

    private Long id;
    private Double minPrice;
    private Double maxPrice;
    private LocalDate availableFrom;
    private LocalDate availableUntil;
    private Long categoryId;
    private NotificationChannel preferredNotificationChannel;
    private String notificationTarget;

    public static List<SearchedProductSubscriptionDto> listOf(List<SearchedProductSubscription> subscriptions) {

        return subscriptions.stream()
                .map(subscription -> SearchedProductSubscriptionDto.builder()
                        .id(subscription.getId())
                        .minPrice(subscription.getMinPrice())
                        .maxPrice(subscription.getMaxPrice())
                        .availableFrom(subscription.getAvailableFrom())
                        .availableUntil(subscription.getAvailableUntil())
                        .categoryId(subscription.getCategory().getId())
                        .preferredNotificationChannel(subscription.getPreferredNotificationChannel())
                        .notificationTarget(subscription.getNotificationTarget())
                        .build()
                ).toList();

    }
}
