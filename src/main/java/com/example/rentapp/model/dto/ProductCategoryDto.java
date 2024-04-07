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
    private List<CategoryRequirementDto> requirements;


    public static ProductCategoryDto of(ProductCategory productCategory) {
        return ProductCategoryDto
                .builder()
                .id(productCategory.getId())
                .name(productCategory.getName())
                .requirements(CategoryRequirementDto.listOf(productCategory.getRequirements()))
                .build();
    }


}
