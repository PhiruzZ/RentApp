package com.example.rentapp.model.dto;

import com.example.rentapp.model.entity.Product;
import com.example.rentapp.model.enums.TimeUnit;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class ProductShortDto {

    private Long id;
    private Long categoryId;
    private Long ownerId;
    private String name;
    private Double price;
    private TimeUnit timeUnitForPrice;

    public static List<ProductShortDto> listOf(List<Product> products) {
        return products.stream()
                .map(product -> ProductShortDto.builder()
                        .id(product.getId())
                        .categoryId(product.getCategory().getId())
                        .ownerId(product.getOwner().getId())
                        .name(product.getName())
                        .price(product.getProductPrice().getBasicPrice())
                        .timeUnitForPrice(product.getProductPrice().getTimeUnitForPrice())
                        .build())
                .toList();
    }
}
