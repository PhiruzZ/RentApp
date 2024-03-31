package com.example.rentapp.repository;

import com.example.rentapp.model.enums.OTPPurpose;
import com.example.rentapp.model.redis.SentOTP;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface SentOTPRepository extends CrudRepository<SentOTP, String> {

    List<SentOTP> findAllByEmail(String email);

    Optional<SentOTP> findByEmail(String email);

    Optional<SentOTP> findByIdAndPurpose(String id, OTPPurpose purpose);

}
