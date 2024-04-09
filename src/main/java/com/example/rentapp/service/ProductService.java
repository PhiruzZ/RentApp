package com.example.rentapp.service;

import com.example.rentapp.model.dto.ProductDto;
import com.example.rentapp.model.dto.ProductShortDto;
import com.example.rentapp.model.embedable.CityItem;
import com.example.rentapp.model.entity.*;
import com.example.rentapp.model.enums.DbStatus;
import com.example.rentapp.model.request.CreateProductRequest;
import com.example.rentapp.model.request.FilterProductsRequest;
import com.example.rentapp.repository.CityRepository;
import com.example.rentapp.repository.ProductPriceRepository;
import com.example.rentapp.repository.ProductPropertiesRepository;
import com.example.rentapp.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductCategoryService productCategoryService;
    private final CategoryRequirementService categoryRequirementService;
    private final AuthService authService;
    private final ProductPropertiesRepository productPropertiesRepository;
    private final CityRepository cityRepository;
    private final ProductPriceRepository productPriceRepository;
    private final SearchedProductSubscriptionService searchedProductSubscriptionService;

    @Transactional
    public void add(CreateProductRequest request) {
        UserEntity loggedUse = authService.getLoggedInUser();
        ProductCategory category = productCategoryService.findById(request.getCategoryId());
        validateRequest(request, category);
        Product product = createProduct(request, category, loggedUse);
        searchedProductSubscriptionService.sendNotifications(product);
    }

    private Product createProduct(CreateProductRequest request, ProductCategory category, UserEntity user) {
        Product product = new Product();
        product.setCategory(category);
        product.setOwner(user);
        product.setName(request.getName());
        product.setAddress(request.getAddress());
        product.setCities(createCityItems(request.getCities()));
        product.setProductPrice(createProductPrice(request));
        product.setAvailableFrom(request.getAvailableFrom());
        product.setAvailableUntil(request.getAvailableUntil());
        product.setAdvancePaymentPercent(request.getAdvancePaymentPercent());
        product.setProperties(createProperties(request.getProperties(), product));
        return productRepository.save(product);
    }

    private ProductPrice createProductPrice(CreateProductRequest request) {
        ProductPrice productPrice = new ProductPrice();
        productPrice.setBasicPrice(request.getBasicPrice());
        productPrice.setTimeUnitForPrice(request.getTimeUnitForPrice());
        return productPriceRepository.save(productPrice);
    }

    private List<CityItem> createCityItems(List<Long> cities) {
        List<CityItem> cityItems = new ArrayList<>();
        for (Long id : cities) {
            Optional<City> city = cityRepository.findByIdAndDbStatus(id, DbStatus.ACTIVE);
            city.ifPresent(value -> cityItems.add(new CityItem(value.getId(), value.getName())));
        }
        return cityItems;
    }

    private List<ProductProperties> createProperties(Map<Long, CreateProductRequest.ProductProperty> request, Product product) {
        List<ProductProperties> productProperties = new ArrayList<>();
        for (Long id: request.keySet()) {
            CategoryRequirement requirement = categoryRequirementService.findById(id);
            productProperties.add(createProperty(requirement, product, request.get(id)));
        }
        return productPropertiesRepository.saveAll(productProperties);
    }

    private ProductProperties createProperty(CategoryRequirement requirement, Product product, CreateProductRequest.ProductProperty request) {
        ProductProperties properties = new ProductProperties();
        properties.setProduct(product);
        properties.setPropertyType(requirement.getPropertyType());
        properties.setPropertyName(requirement.getPropertyName());
        properties.setPropertyValue(request.getValue().getValue());
        return properties;
    }

    private void validateRequest(CreateProductRequest request, ProductCategory category) {
        List<CategoryRequirement> requirements = category.getRequirements();
        for (CategoryRequirement r : requirements){
            if(request.getProperties().containsKey(r.getId())){
                if(!r.getPossibleValues().contains(request.getProperties().get(r.getId()).getValue())){
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid value for property. name: " + r.getPropertyName() + "id: " + r.getId());
                }
            }else if (r.getRequired()){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Required property is missing. name: " + r.getPropertyName() + "id: " + r.getId());
            }
        }
    }

    public ProductDto getById(Long id) {
        Product product = findById(id);
        return ProductDto.of(product);
    }

    public Product findById(Long id) {
        return productRepository.findByIdAndDbStatus(id, DbStatus.ACTIVE)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public List<ProductShortDto> filter(FilterProductsRequest request) {
        List<Product> products = cityRepository.filter(request);
        return ProductShortDto.listOf(products);
    }

    public Double calcPriceForDates(ProductPrice productPrice, LocalDate from, LocalDate until) {
        return productPrice.getBasicPrice();
    }
}
