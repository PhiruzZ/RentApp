package com.example.rentapp.controller;

import com.example.rentapp.model.request.AuthRequest;
import com.example.rentapp.model.request.RegistrationRequest;
import com.example.rentapp.model.request.SendOTPRequest;
import com.example.rentapp.model.request.VerifyOTPRequest;
import com.example.rentapp.model.response.AuthResponse;
import com.example.rentapp.model.response.VerifyOTPResponse;
import com.example.rentapp.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthRequest authRequest) {
        return ResponseEntity.ok(authService.login(authRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid RegistrationRequest request) {
        return ResponseEntity.ok(authService.registerUser(request));
    }

    @PostMapping("/send-otp")
    public ResponseEntity<Void> sendOTP(@RequestBody @Valid SendOTPRequest request){
        authService.sendOTP(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<VerifyOTPResponse> verifyOTP(@RequestBody @Valid VerifyOTPRequest request){
        return ResponseEntity.ok(authService.verifyOTP(request));
    }

}
