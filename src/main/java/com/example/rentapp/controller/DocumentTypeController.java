package com.example.rentapp.controller;

import com.example.rentapp.model.dto.DocumentTypeDto;
import com.example.rentapp.model.request.CreateDocumentTypeRequest;
import com.example.rentapp.service.DocumentTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/document/type")
public class DocumentTypeController {

    private final DocumentTypeService documentTypeService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> addDocumentType(@RequestBody @Valid CreateDocumentTypeRequest request){
        documentTypeService.addDocumentType(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@RequestParam Long id){
        documentTypeService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<DocumentTypeDto>> getAll(){
        return ResponseEntity.ok(documentTypeService.getAll());
    }

    @GetMapping("/verification/allowed")
    public ResponseEntity<List<DocumentTypeDto>> getForVerification(){
        return ResponseEntity.ok(documentTypeService.getForVerification());
    }


}
