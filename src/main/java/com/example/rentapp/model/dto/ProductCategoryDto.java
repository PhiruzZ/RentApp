package com.example.rentapp.model.dto;

import com.example.rentapp.model.entity.ProductCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class ProductCategoryDto {

    private Long id;
    private String name;

    public static List<ProductCategoryDto> listOf(List<ProductCategory> productCategories) {
        return productCategories
                .stream()
                .map(productCategory ->
                        ProductCategoryDto
                                .builder()
                                .id(productCategory.getId())
                                .name(productCategory.getName())
                                .build()
                ).toList();

    }
}
