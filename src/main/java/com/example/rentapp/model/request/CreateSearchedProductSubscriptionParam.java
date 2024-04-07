package com.example.rentapp.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class CreateSearchedProductSubscriptionParam {

    private Double minPrice;
    private Double maxPrice;
    private LocalDate availableFrom;
    private LocalDate availableUntil;

    @NotNull
    private Long categoryId;

}
