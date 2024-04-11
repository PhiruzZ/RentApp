package com.example.rentapp.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TransactionRequest {

    @NotNull
    private Double amount;
    @NotNull
    private boolean saveCard;

}
