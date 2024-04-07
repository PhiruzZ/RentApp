package com.example.rentapp.model.dto;

import com.example.rentapp.model.entity.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@Builder
public class ProductDto {

    private Long categoryId;
    private Long ownerId;
    private String name;
    private Double price;
    private List<CityDto> cities;
    private String address;
    private LocalDate availableFrom;
    private LocalDate availableUntil;
    private Double advancePaymentPercent;
    private List<ProductPropertiesDto> properties;

    public static ProductDto of(Product product) {
        return ProductDto.builder()
                .categoryId(product.getCategory().getId())
                .ownerId(product.getOwner().getId())
                .name(product.getName())
                .price(product.getProductPrice().getBasicPrice())
                .cities(CityDto.listOf(product.getCities()))
                .address(product.getAddress())
                .availableFrom(product.getAvailableFrom())
                .availableUntil(product.getAvailableUntil())
                .advancePaymentPercent(product.getAdvancePaymentPercent())
                .properties(ProductPropertiesDto.listOf(product.getProperties()))
                .build();
    }
}
