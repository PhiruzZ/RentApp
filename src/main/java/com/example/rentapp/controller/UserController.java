package com.example.rentapp.controller;

import com.example.rentapp.model.dto.UserDto;
import com.example.rentapp.model.request.UpdateUserRequest;
import com.example.rentapp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;


    @PostMapping("/block")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> blockUser(@RequestParam Long id){
        userService.blockUser(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/unblock")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> unblockUser(@RequestParam Long id){
        userService.unblockUser(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verification/request")
    public ResponseEntity<Void> requestVerification(@RequestParam Long userDocumentId){
        userService.requestVerification(userDocumentId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/verification/pending")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UserDto>> getPendingVerificationUsers(){
        return ResponseEntity.ok(userService.getPendingVerificationUsers());
    }

    @PostMapping("/verification/confirm")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> confirmVerification(@RequestParam Long id){
        userService.confirmVerification(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verification/reject")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> rejectVerification(@RequestParam Long id){
        userService.rejectVerification(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<UserDto> get(){
        return ResponseEntity.ok(userService.getLoggedUser());
    }

    @PutMapping
    public ResponseEntity<UserDto> update(@RequestBody @Valid UpdateUserRequest request){
        return ResponseEntity.ok(userService.update(request));
    }

}
