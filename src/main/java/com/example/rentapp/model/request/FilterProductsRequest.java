package com.example.rentapp.model.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class FilterProductsRequest {

    private Long categoryId;
    private String name;
    private Double minPrice;
    private Double maxPrice;
    private LocalDate availableFrom;
    private LocalDate availableUntil;


}
