package com.example.rentapp.service;

import com.example.rentapp.model.dto.ProductCategoryDto;
import com.example.rentapp.model.dto.ProductCategoryShortDto;
import com.example.rentapp.model.entity.ProductCategory;
import com.example.rentapp.model.enums.DbStatus;
import com.example.rentapp.model.request.CreateProductCategoryRequest;
import com.example.rentapp.repository.ProductCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;
    private final CategoryRequirementService categoryRequirementService;


    @Transactional
    public void add(CreateProductCategoryRequest request) {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setName(request.getName());
        productCategoryRepository.save(productCategory);
        categoryRequirementService.createAll(productCategory, request.getRequirements());
    }

    @Transactional
    public void delete(Long id) {
        ProductCategory productCategory = findById(id);
        productCategory.setDbStatus(DbStatus.DELETED);
        categoryRequirementService.deleteByProductCategory(productCategory.getId());
    }

    public ProductCategory findById(Long id) {
        return productCategoryRepository.findByIdAndDbStatus(id, DbStatus.ACTIVE)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found category"));
    }

    public List<ProductCategoryShortDto> getAll() {
        List<ProductCategory> productCategories = productCategoryRepository.findByDbStatus(DbStatus.ACTIVE);
        return ProductCategoryShortDto.listOf(productCategories);
    }

    public ProductCategoryDto getById(Long id) {
        ProductCategory productCategory = productCategoryRepository.findByIdAndDbStatus(id, DbStatus.ACTIVE)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found product category!"));
        return ProductCategoryDto.of(productCategory);

    }
}
