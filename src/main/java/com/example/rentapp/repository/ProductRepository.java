package com.example.rentapp.repository;

import com.example.rentapp.model.entity.Product;
import com.example.rentapp.model.enums.DbStatus;
import com.example.rentapp.model.enums.ProductStatus;
import com.example.rentapp.model.request.FilterProductsRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByIdAndDbStatus(Long id, DbStatus dbStatus);

    Optional<Product> findByIdAndProductStatus(Long productId, ProductStatus productStatus);

    @Query("""
        select p
        from Product p
        where (:#{#request.getCategoryId()} is null or p.category.id = :#{#request.getCategoryId()})
        and (:#{#request.getName()} is null or lower(p.name) like %:#{#request.getName()}%)
        and (:#{#request.getMinPrice()} is null or p.productPrice.basicPrice >= :#{#request.getMinPrice()})
        and (:#{#request.getMaxPrice()} is null or p.productPrice.basicPrice <= :#{#request.getMaxPrice()})
        and (cast(:#{#request.getAvailableFrom()} as date) is null or p.availableFrom <= :#{#request.getAvailableFrom()})
        and (cast(:#{#request.getAvailableUntil()} as date) is null or p.availableUntil >= :#{#request.getAvailableUntil()})
        and p.productStatus = :status
        and p.dbStatus = :dbStatus
    """)
    List<Product> filter(FilterProductsRequest request, ProductStatus status, DbStatus dbStatus);

    Optional<Product> findByIdAndDbStatusAndOwnerId(Long productId, DbStatus dbStatus, Long userId);
}
