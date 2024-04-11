package com.example.rentapp.controller;

import com.example.rentapp.model.dto.UserDocumentDto;
import com.example.rentapp.service.UserDocumentService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
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
                                            @RequestPart MultipartFile document){
        userDocumentService.addDocument(documentTypeId, document);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteDocument(@RequestParam Long id){
        userDocumentService.deleteDocument(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDocumentDto>> getAll(){
        return ResponseEntity.ok(userDocumentService.getAll());
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UserDocumentDto>> getByUserId(@PathVariable Long userId){
        return ResponseEntity.ok(userDocumentService.getByUserId(userId));
    }

    @GetMapping("/by_id")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<byte[]> getById(@RequestParam Long id){
        return ResponseEntity.ok(userDocumentService.getById(id));
    }

}
