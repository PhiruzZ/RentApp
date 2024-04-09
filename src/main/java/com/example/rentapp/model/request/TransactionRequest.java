package com.example.rentapp.model.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TransactionRequest {

    private Double amount;
    private boolean saveCard;

}
