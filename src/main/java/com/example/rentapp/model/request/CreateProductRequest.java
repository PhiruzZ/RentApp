package com.example.rentapp.model.request;

import com.example.rentapp.model.embedable.PropertyValue;
import com.example.rentapp.model.enums.TimeUnit;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Setter
@Getter
public class CreateProductRequest {

    private Long categoryId;
    private String name;
    private String address;
    private List<Long> cities;
    private LocalDate availableFrom;
    private LocalDate availableUntil;
    private Double advancePaymentPercent;
    private Map<Long, ProductProperty> properties;

    private Double basicPrice;
    private TimeUnit timeUnitForPrice;

    @Setter
    @Getter
    public static class ProductProperty{

        private PropertyValue value;
    }

}
