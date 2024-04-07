package com.example.rentapp.repository;

import com.example.rentapp.model.entity.SearchedProductSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface SearchedProductSubscriptionRepository extends JpaRepository<SearchedProductSubscription, Long> {

    @Query("""
                select s
                from SearchedProductSubscription s
                where (s.availableFrom is null or s.availableFrom = :availableFrom)
                and (s.availableUntil is null or s.availableUntil = :availableUntil)
                and ((s.minPrice is null or :price >= s.minPrice)
                and (s.maxPrice is null or :price <= s.maxPrice))
                and (s.category.id = :categoryId)
            """)
    List<SearchedProductSubscription> findProductMatches(Double price, LocalDate availableFrom, LocalDate availableUntil, Long categoryId);
}
