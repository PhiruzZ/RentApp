package com.example.rentapp.model.entity;

import com.example.rentapp.model.enums.Currency;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "user_balance")
public class UserBalance extends BaseEntity{

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "available_amount")
    private Double availableAmount;

    @Column(name = "blocked_amount")
    private Double blockedAmount;

    @Column(name = "currency")
    @Enumerated(EnumType.STRING)
    private Currency currency;

}
