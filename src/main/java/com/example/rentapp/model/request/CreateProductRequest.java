package com.example.rentapp.model.request;

import com.example.rentapp.model.embedable.PropertyValue;
import com.example.rentapp.model.enums.TimeUnit;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Setter
@Getter
public class CreateProductRequest {

    @NotNull
    private Long categoryId;
    @NotBlank
    private String name;
    @NotBlank
    private String address;
    @NotEmpty
    private List<Long> cities;
    @NotNull
    private LocalDate availableFrom;
    @NotNull
    private LocalDate availableUntil;
    private Double advancePaymentPercent;
    private Map<Long, ProductProperty> properties;

    @NotNull
    private Double basicPrice;
    @NotNull
    private TimeUnit timeUnitForPrice;

    @Setter
    @Getter
    public static class ProductProperty{

        private PropertyValue value;
    }

}
