package com.example.rentapp.controller;

import com.example.rentapp.model.dto.SearchedProductSubscriptionDto;
import com.example.rentapp.model.request.CreateSearchedProductSubscriptionParam;
import com.example.rentapp.service.SearchedProductSubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.List;

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

    @DeleteMapping
    ResponseEntity<Void> delete(@RequestParam Long id){
        searchedProductSubscriptionService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all")
    ResponseEntity<List<SearchedProductSubscriptionDto>> getByUser(){
        return ResponseEntity.ok(searchedProductSubscriptionService.getByUser());
    }

}
