package com.example.rentapp.model.dto;

import com.example.rentapp.model.entity.Transaction;
import com.example.rentapp.model.enums.TransactionStatus;
import lombok.Builder;

import java.util.List;

@Builder
public class TransactionDto {

    private String fromUser;
    private String toUser;
    private Double amount;
    private TransactionStatus status;

    public static List<TransactionDto> listOf(List<Transaction> transactions) {
        return transactions.stream()
                .map(transaction -> TransactionDto.builder()
                        .fromUser(transaction.getFromBalance() != null ? transaction.getFromBalance().getUser().getFirstName() : null)
                        .toUser(transaction.getToBalance() != null ? transaction.getToBalance().getUser().getFirstName() : null)
                        .amount(transaction.getAmount())
                        .status(transaction.getTransactionStatus())
                        .build())
                .toList();

    }


}
