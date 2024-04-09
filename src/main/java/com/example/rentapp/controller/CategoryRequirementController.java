package com.example.rentapp.controller;

import com.example.rentapp.model.dto.CategoryRequirementDto;
import com.example.rentapp.model.embedable.PropertyValue;
import com.example.rentapp.model.request.AddCategoryRequirementsRequest;
import com.example.rentapp.model.request.PredefinedValueRequest;
import com.example.rentapp.service.CategoryRequirementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/category/requirement")
public class CategoryRequirementController {

    private final CategoryRequirementService categoryRequirementService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> addRequirements(@RequestBody AddCategoryRequirementsRequest request){
        categoryRequirementService.addRequirements(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> removeRequirement(@RequestParam Long id){
        categoryRequirementService.removeRequirement(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<CategoryRequirementDto>> getByCategory(@RequestParam Long categoryId){
        return ResponseEntity.ok(categoryRequirementService.getByCategory(categoryId));
    }

    @PostMapping("/predefined/value")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> addValues(@RequestBody PredefinedValueRequest request){
        categoryRequirementService.addPredefinedValue(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/predefined/value")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteValues(@RequestBody PredefinedValueRequest request){
        categoryRequirementService.deletePredefinedValues(request);
        return ResponseEntity.ok().build();
    }

}
