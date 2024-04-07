package com.example.rentapp.repository;

import com.example.rentapp.model.entity.AgreementRequest;
import com.example.rentapp.model.enums.AgreementRequestStatus;
import com.example.rentapp.model.enums.DbStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AgreementRequestRepository extends JpaRepository<AgreementRequest, Long> {

    @Query("""
       select a from AgreementRequest a
       where a.product.id = :productId
       and a.status = :status
       and a.dbStatus = :dbStatus
       limit 1
""")
    Optional<AgreementRequest> findApprovedRequestForProduct(Long productId, AgreementRequestStatus status, DbStatus dbStatus);

    Optional<AgreementRequest> findByIdAndDbStatusAndFromUserId(Long id, DbStatus dbStatus, Long userId);

    List<AgreementRequest> findByProductIdAndDbStatusAndStatus(Long productId, DbStatus dbStatus, AgreementRequestStatus agreementRequestStatus);
}
