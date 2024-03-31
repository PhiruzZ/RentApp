package com.example.rentapp.model.entity;

import com.example.rentapp.model.enums.PaymentProvider;
import com.example.rentapp.model.enums.TransactionStatus;
import com.example.rentapp.model.enums.TransactionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "transaction")
public class Transaction extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "from_balance_id")
    private UserBalance fromBalance;

    @ManyToOne
    @JoinColumn(name = "to_balance_id")
    private UserBalance toBalance;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "transaction_type")
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(name = "payment_provider")
    @Enumerated(EnumType.STRING)
    private PaymentProvider paymentProvider;

    @Column(name = "provider_transaction_id")
    private String providerTransactionId;

    @Column(name = "transaction_status")
    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;

}
