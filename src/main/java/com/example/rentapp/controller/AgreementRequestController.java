package com.example.rentapp.controller;

import com.example.rentapp.model.dto.AgreementRequestDto;
import com.example.rentapp.model.request.CreateAgreementRequestRequest;
import com.example.rentapp.service.AgreementRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/agreement/request")
public class AgreementRequestController {

    private final AgreementRequestService agreementRequestService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody CreateAgreementRequestRequest request){
        agreementRequestService.create(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/approve")
    public ResponseEntity<Void> approve(@RequestParam Long id){
        agreementRequestService.approve(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/reject")
    public ResponseEntity<Void> reject(@RequestParam Long id){
        agreementRequestService.reject(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/finalise")
    public ResponseEntity<Void> finalise(@RequestParam Long id){
        agreementRequestService.finalise(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/cancel")
    public ResponseEntity<Void> cancel(@RequestParam Long id){
        agreementRequestService.cancel(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/product")
    public ResponseEntity<List<AgreementRequestDto>> getByProduct(@RequestParam Long productId){
        return ResponseEntity.ok(agreementRequestService.findByProduct(productId));
    }

    @GetMapping("/sent")
    public ResponseEntity<List<AgreementRequestDto>> getByUser(){
        return ResponseEntity.ok(agreementRequestService.getByUser());
    }
}
