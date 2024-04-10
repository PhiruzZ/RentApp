package com.example.rentapp.controller;

import com.example.rentapp.model.dto.UserDocumentDto;
import com.example.rentapp.service.UserDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/document")
public class UserDocumentController {

    private final UserDocumentService userDocumentService;

    @PostMapping
    public ResponseEntity<Void> addDocument(@RequestParam Long documentTypeId,
                                            @RequestParam MultipartFile document){
        userDocumentService.addDocument(documentTypeId, document);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteDocument(@RequestParam Long id){
        userDocumentService.deleteDocument(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<UserDocumentDto>> getAll(){
        return ResponseEntity.ok(userDocumentService.getAll());
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UserDocumentDto>> getByUserId(@PathVariable Long userId){
        return ResponseEntity.ok(userDocumentService.getByUserId(userId));
    }

}
