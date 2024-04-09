package com.example.rentapp.controller;

import com.example.rentapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<Void> requestVerification(@RequestParam MultipartFile document,
                                                    @RequestParam Long documentTypeId){
        userService.requestVerification(document, documentTypeId);
        return ResponseEntity.ok().build();
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

}
