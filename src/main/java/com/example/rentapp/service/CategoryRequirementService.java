package com.example.rentapp.service;

import com.example.rentapp.model.dto.CategoryRequirementDto;
import com.example.rentapp.model.entity.CategoryRequirement;
import com.example.rentapp.model.entity.DocumentType;
import com.example.rentapp.model.entity.ProductCategory;
import com.example.rentapp.model.enums.DbStatus;
import com.example.rentapp.model.request.AddCategoryRequirementsRequest;
import com.example.rentapp.repository.CategoryRequirementRepository;
import com.example.rentapp.repository.ProductCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryRequirementService {

    private final CategoryRequirementRepository categoryRequirementRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final DocumentTypeService documentTypeService;

    public void createAll(ProductCategory productCategory, List<Long> documentTypeIds) {
        List<CategoryRequirement> categoryRequirements = documentTypeIds
                .stream()
                .map(documentTypeId -> create(productCategory, documentTypeId))
                .toList();
        categoryRequirementRepository.saveAll(categoryRequirements);
    }

    private CategoryRequirement create(ProductCategory productCategory, Long documentTypeId) {
        CategoryRequirement requirement = new CategoryRequirement();
        requirement.setCategory(productCategory);
        DocumentType documentType = documentTypeService.getById(documentTypeId);
        requirement.setDocumentType(documentType);
        return requirement;
    }

    @Transactional
    public void addRequirements(AddCategoryRequirementsRequest request) {
        ProductCategory category = productCategoryRepository.findByIdAndDbStatus(request.getCategoryId(), DbStatus.ACTIVE)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found category!"));
        createAll(category, request.getDocumentTypeIds());

    }

    @Transactional
    public void removeRequirement(Long id) {
        CategoryRequirement requirement = categoryRequirementRepository.findByIdAndDbStatus(id, DbStatus.ACTIVE)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found requirement!"));
        requirement.setDbStatus(DbStatus.DELETED);
        categoryRequirementRepository.save(requirement);
    }

    public List<CategoryRequirementDto> getByCategory(Long categoryId) {
        List<CategoryRequirement> categoryRequirements = categoryRequirementRepository.findByCategoryIdAndDbStatus(categoryId, DbStatus.ACTIVE);
        return CategoryRequirementDto.listOf(categoryRequirements);
    }

    public void deleteByProductCategory(Long categoryId) {
        List<CategoryRequirement> categoryRequirements = categoryRequirementRepository.findByCategoryIdAndDbStatus(categoryId, DbStatus.ACTIVE);
        categoryRequirements = categoryRequirements.stream()
                .peek(categoryRequirement -> categoryRequirement.setDbStatus(DbStatus.DELETED))
                .toList();
        categoryRequirementRepository.saveAll(categoryRequirements);
    }
}
