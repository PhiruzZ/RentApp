package com.example.rentapp.repository;

import com.example.rentapp.model.entity.UserBalance;
import com.example.rentapp.model.enums.DbStatus;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import java.util.Optional;

public interface UserBalanceRepository extends JpaRepository<UserBalance, Long> {

    @Query("""
        select b
        from UserBalance b
        where b.id = :id
        and b.dbStatus = :status
    """)
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "10000")})
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<UserBalance> findByIdAndDbStatusAndLock(Long id, DbStatus status);

    Optional<UserBalance> findByIdAndDbStatus(Long balanceId, DbStatus dbStatus);
}
