package com.example.rentapp.controller;

import com.example.rentapp.model.dto.ProductCategoryDto;
import com.example.rentapp.model.dto.ProductCategoryShortDto;
import com.example.rentapp.model.request.CreateProductCategoryRequest;
import com.example.rentapp.service.ProductCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product/category")
public class ProductCategoryController {

    private final ProductCategoryService productCategoryService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> add(@RequestBody @Valid CreateProductCategoryRequest request){
        productCategoryService.add(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@RequestParam Long id){
        productCategoryService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductCategoryShortDto>> getAll(){
        return ResponseEntity.ok(productCategoryService.getAll());
    }

    @GetMapping
    public ResponseEntity<ProductCategoryDto> get(@RequestParam Long id){
        return ResponseEntity.ok(productCategoryService.getById(id));
    }

}
