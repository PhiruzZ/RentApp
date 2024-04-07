package com.example.rentapp.model.dto;

import com.example.rentapp.model.entity.ProductCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class ProductCategoryShortDto {

    private Long id;
    private String name;

    public static List<ProductCategoryShortDto> listOf(List<ProductCategory> productCategories) {
        return productCategories
                .stream()
                .map(productCategory ->
                        ProductCategoryShortDto
                                .builder()
                                .id(productCategory.getId())
                                .name(productCategory.getName())
                                .build()
                ).toList();

    }
}
