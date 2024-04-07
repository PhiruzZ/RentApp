package com.example.rentapp.model.dto;

import com.example.rentapp.model.entity.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class ProductShortDto {

    private Long categoryId;
    private Long ownerId;
    private String name;
    private Double price;

    public static List<ProductShortDto> listOf(List<Product> products) {
        return products.stream()
                .map(product -> ProductShortDto.builder()
                        .categoryId(product.getCategory().getId())
                        .ownerId(product.getOwner().getId())
                        .name(product.getName())
                        .price(product.getProductPrice().getBasicPrice())
                        .build())
                .toList();
    }
}
