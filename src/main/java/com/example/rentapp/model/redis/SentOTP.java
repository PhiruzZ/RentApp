package com.example.rentapp.model.redis;

import com.example.rentapp.model.enums.OTPPurpose;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.util.UUID;

@RedisHash(value = "sentOTP",timeToLive = 300L)
@Data
public class SentOTP implements Serializable {

    private String id;
    private String otp;
    @Indexed
    private String email;
    @Indexed
    private OTPPurpose purpose;
    private boolean verified;

    public SentOTP(String otp, String email, OTPPurpose purpose){
        this.id = UUID.randomUUID().toString();
        this.otp = otp;
        this.email = email;
        this.purpose = purpose;
        this.verified = false;
    }

}
