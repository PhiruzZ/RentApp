package com.example.rentapp.controller;

import com.example.rentapp.model.request.TransactionRequest;
import com.example.rentapp.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/topup")
    public ResponseEntity<Void> topUp(@RequestBody TransactionRequest request){
        transactionService.topUp(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/cashout")
    public ResponseEntity<Void> cashOut(@RequestBody TransactionRequest request){
        transactionService.cashOut(request);
        return ResponseEntity.ok().build();
    }

}
