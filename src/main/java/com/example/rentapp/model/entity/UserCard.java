package com.example.rentapp.model.entity;

import com.example.rentapp.model.enums.PaymentProvider;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "user_card")
public class UserCard extends BaseEntity{

    @Column(name = "card_mask")
    private String cardMask;

    @Column(name = "payment_provider")
    @Enumerated(EnumType.STRING)
    private PaymentProvider paymentProvider;

    @Column(name = "provider_transaction_id")
    private String providerTransactionId;

}
