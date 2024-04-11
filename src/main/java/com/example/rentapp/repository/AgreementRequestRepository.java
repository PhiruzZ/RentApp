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
""")
    Optional<AgreementRequest> findApprovedRequestForProduct(Long productId, AgreementRequestStatus status, DbStatus dbStatus);

    @Query("""
        select a
        from AgreementRequest a
        where a.id = :id
        and a.dbStatus = :dbStatus
        and (a.fromUser.id = :userId or a.product.owner.id = :userId)
""")
    Optional<AgreementRequest> findByIdAndDbStatusAndUserId(Long id, DbStatus dbStatus, Long userId);

    List<AgreementRequest> findByProductIdAndDbStatusAndStatus(Long productId, DbStatus dbStatus, AgreementRequestStatus agreementRequestStatus);

    Optional<AgreementRequest> findByIdAndDbStatusAndFromUserId(Long id, DbStatus dbStatus, Long userId);

    @Query("""
        select a
        from AgreementRequest a
        where a.id = :id
        and a.dbStatus = :dbStatus
        and (a.product.owner.id = :ownerId)
    """)
    Optional<AgreementRequest> findByIdAndOwnerIdAndDbStatus(Long id, Long ownerId, DbStatus dbStatus);

    List<AgreementRequest> findByProductIdAndDbStatus(Long productId, DbStatus dbStatus);

    List<AgreementRequest> findByFromUserIdAndDbStatus(Long userId, DbStatus dbStatus);

}
