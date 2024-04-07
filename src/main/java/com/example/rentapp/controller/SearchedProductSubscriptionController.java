package com.example.rentapp.controller;

import com.example.rentapp.model.request.CreateSearchedProductSubscriptionParam;
import com.example.rentapp.service.SearchedProductSubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product/subscription")
public class SearchedProductSubscriptionController {

    private final SearchedProductSubscriptionService searchedProductSubscriptionService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody CreateSearchedProductSubscriptionParam param){
        searchedProductSubscriptionService.create(param);
        return ResponseEntity.ok().build();
    }

}
