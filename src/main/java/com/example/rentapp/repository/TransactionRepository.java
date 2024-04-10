package com.example.rentapp.repository;

import com.example.rentapp.model.entity.Transaction;
import com.example.rentapp.model.enums.DbStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {


    @Query("""
        select t
        from Transaction t
        where (t.fromBalance.user.id = :userId
        or t.toBalance.user.id = :userId)
        and t.dbStatus = :dbStatus
    """)
    List<Transaction> findByUserIdAndDbStatus(Long userId, DbStatus dbStatus);

    Optional<Transaction> findByAgreementRequestIdAndDbStatus(Long id, DbStatus dbStatus);
}
