package com.example.rentapp.model.entity;

import com.example.rentapp.model.enums.NotificationChannel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Entity
@Table(name = "searched_product-subscription")
public class SearchedProductSubscription extends BaseEntity{

    @Column(name = "min_price")
    private Double minPrice;

    @Column(name = "max_price")
    private Double maxPrice;

    @Column(name = "available_from")
    private LocalDate availableFrom;

    @Column(name = "available_until")
    private LocalDate availableUntil;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private ProductCategory category;

    @Column(name = "preferred_notification_channel")
    @Enumerated(EnumType.STRING)
    private NotificationChannel preferredNotificationChannel;

    @Column(name = "notification_target")
    private String notificationTarget;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

}
