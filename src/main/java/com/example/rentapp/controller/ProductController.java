package com.example.rentapp.controller;

import com.example.rentapp.model.dto.ProductDto;
import com.example.rentapp.model.dto.ProductShortDto;
import com.example.rentapp.model.request.CreateProductRequest;
import com.example.rentapp.model.request.FilterProductsRequest;
import com.example.rentapp.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<Void> add(@RequestBody CreateProductRequest request){
        productService.add(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/filter")
    public ResponseEntity<List<ProductShortDto>> filter(@RequestBody FilterProductsRequest request){
        return ResponseEntity.ok(productService.filter(request));
    }

    @GetMapping
    public ResponseEntity<ProductDto> get(@RequestParam Long id){
        return ResponseEntity.ok(productService.getById(id));
    }

}
