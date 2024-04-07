package com.example.rentapp.service;

import com.example.rentapp.model.dto.CategoryRequirementDto;
import com.example.rentapp.model.embedable.PropertyValue;
import com.example.rentapp.model.entity.CategoryRequirement;
import com.example.rentapp.model.entity.ProductCategory;
import com.example.rentapp.model.enums.DbStatus;
import com.example.rentapp.model.request.AddCategoryRequirementsRequest;
import com.example.rentapp.model.request.CreateCategoryRequirementRequest;
import com.example.rentapp.model.request.PredefinedValueRequest;
import com.example.rentapp.model.request.CreatePropertyValueRequest;
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

    public void createAll(ProductCategory productCategory, List<CreateCategoryRequirementRequest> requirements) {
        List<CategoryRequirement> categoryRequirements = requirements
                .stream()
                .map(requirement -> create(productCategory, requirement))
                .toList();
        categoryRequirementRepository.saveAll(categoryRequirements);
    }

    private CategoryRequirement create(ProductCategory productCategory, CreateCategoryRequirementRequest request) {
        CategoryRequirement requirement = new CategoryRequirement();
        requirement.setCategory(productCategory);
        requirement.setPropertyName(request.getPropertyName());
        requirement.setPropertyType(request.getPropertyType());
        requirement.setRequired(request.getRequired());
        requirement.setValuesPredefined(request.getValuesPredefined());
        requirement.setPossibleValues(createValues(request.getPossibleValues()));
        return requirement;
    }

    private List<PropertyValue> createValues(List<CreatePropertyValueRequest> possibleValues) {
        return possibleValues.stream()
                .map(PropertyValue::new)
                .toList();
    }

    @Transactional
    public void addRequirements(AddCategoryRequirementsRequest request) {
        ProductCategory category = productCategoryRepository.findByIdAndDbStatus(request.getCategoryId(), DbStatus.ACTIVE)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found category!"));
        createAll(category, request.getRequirements());
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

    @Transactional
    public void addPredefinedValue(PredefinedValueRequest request) {
        CategoryRequirement requirement = categoryRequirementRepository.findByIdAndDbStatus(request.getRequirementId(), DbStatus.ACTIVE)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found requirement!"));
        requirement.getPossibleValues().addAll(request.getValues());
        categoryRequirementRepository.save(requirement);
    }


    public void deletePredefinedValues(PredefinedValueRequest request) {
        CategoryRequirement requirement = categoryRequirementRepository.findByIdAndDbStatus(request.getRequirementId(), DbStatus.ACTIVE)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found requirement!"));
        requirement.getPossibleValues().removeAll(request.getValues());
        categoryRequirementRepository.save(requirement);
    }

    public CategoryRequirement findById(Long id) {
        return categoryRequirementRepository.findByIdAndDbStatus(id, DbStatus.ACTIVE)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found category requirement with id: " + id));
    }
}
