package com.example.rentapp.service;

import com.example.rentapp.model.enums.OTPPurpose;
import com.example.rentapp.model.redis.SentOTP;
import com.example.rentapp.repository.SentOTPRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class SentOTPService {

    private final SentOTPRepository sentOTPRepository;

    public void clearPreviousOTPs(String email) {
        List<SentOTP> sentOTPList = sentOTPRepository.findAllByEmail(email);
        sentOTPRepository.deleteAll(sentOTPList);
    }

    public void save(SentOTP sentOTP) {
        sentOTPRepository.save(sentOTP);
    }

    public SentOTP findVerifiedForLoginById(String id, OTPPurpose purpose){
        SentOTP sentOTP = sentOTPRepository.findById(id)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "OTP not found"));
        if(!sentOTP.isVerified()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Verified OTP not found");
        }
        return sentOTP;
    }

    public SentOTP findNotVerifiedOTPByMail(String email) {
        SentOTP sentOTP = sentOTPRepository.findByEmail(email)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "OTP not sent for email!"));
        if(sentOTP.isVerified()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "OTP not sent for email!");
        }
        return sentOTP;
    }

    public void delete(SentOTP sentOTP) {
        sentOTPRepository.delete(sentOTP);
    }
}
