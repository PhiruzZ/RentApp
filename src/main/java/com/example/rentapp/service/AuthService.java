package com.example.rentapp.service;

import com.example.rentapp.model.entity.UserEntity;
import com.example.rentapp.model.enums.DbStatus;
import com.example.rentapp.model.enums.OTPPurpose;
import com.example.rentapp.model.enums.UserRole;
import com.example.rentapp.model.redis.SentOTP;
import com.example.rentapp.model.request.AuthRequest;
import com.example.rentapp.model.request.RegistrationRequest;
import com.example.rentapp.model.request.SendOTPRequest;
import com.example.rentapp.model.request.VerifyOTPRequest;
import com.example.rentapp.model.response.AuthResponse;
import com.example.rentapp.model.response.VerifyOTPResponse;
import com.example.rentapp.repository.UserRepository;
import com.example.rentapp.util.JwtUtils;
import com.example.rentapp.util.OTPUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final SentOTPService sentOTPService;
    private final EmailService emailService;

    @Transactional
    public AuthResponse login(AuthRequest authRequest) {

        SentOTP sentOTP = sentOTPService.findVerifiedForLoginById(authRequest.getVerificationId(), OTPPurpose.LOGIN);
        sentOTPService.delete(sentOTP);
        UserEntity user = userRepository.findByEmailAndDbStatus(sentOTP.getEmail(), DbStatus.ACTIVE)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Incorrect email"));
        boolean matches = passwordEncoder.matches(authRequest.getPassword(), user.getPassword());
        if(!matches) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Incorrect credentials!");
        }
        return jwtUtils.generateJwtToken(user);
    }

    @Transactional
    public AuthResponse registerUser(RegistrationRequest request) {

        SentOTP sentOTP = sentOTPService.findVerifiedForLoginById(request.getVerificationId(), OTPPurpose.REGISTRATION);
        sentOTPService.delete(sentOTP);

        Optional<UserEntity> existingUser = userRepository.findByEmailAndDbStatus(sentOTP.getEmail(), DbStatus.ACTIVE);
        if(existingUser.isPresent()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with email already exists!");
        }

        UserEntity user = new UserEntity();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(sentOTP.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRole(UserRole.USER);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        return  jwtUtils.generateJwtToken(user);
    }

    @Transactional
    public void sendOTP(SendOTPRequest request) {
        if(request.getPurpose().equals(OTPPurpose.LOGIN)){
            sendLoginOTP(request);
        }else {
            sendRegistrationOTP(request);
        }
    }

    private void sendRegistrationOTP(SendOTPRequest request) {
        Optional<UserEntity> optionalUser = userRepository.findByEmailAndDbStatus(request.getEmail(), DbStatus.ACTIVE);
        if(optionalUser.isPresent()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists with email!");
        }
        generateAndSendOTP(request.getEmail(), OTPPurpose.REGISTRATION);
    }

    private void sendLoginOTP(SendOTPRequest request) {
        UserEntity user = userRepository.findByEmailAndDbStatus(request.getEmail(), DbStatus.ACTIVE)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with email!"));
        generateAndSendOTP(user.getEmail(), OTPPurpose.LOGIN);
    }

    private void generateAndSendOTP(String email, OTPPurpose otpPurpose) {
        String otp = OTPUtils.randomCode();
        String emailText = "Your one time password for RentApp: " + otp +  ".\n" +
                "It will expire in 5 minutes!" ;
        sentOTPService.clearPreviousOTPs(email);
        SentOTP sentOTP = new SentOTP(otp, email, otpPurpose);
        sentOTPService.save(sentOTP);
        emailService.sendMail(emailText, email);
    }


    public VerifyOTPResponse verifyOTP(VerifyOTPRequest request) {
        SentOTP sentOTP = sentOTPService.findNotVerifiedOTPByMail(request.getEmail());
        if(!sentOTP.getOtp().equals(request.getOtp())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong OTP provided!");
        }
        sentOTP.setVerified(true);
        sentOTPService.save(sentOTP);
        return new VerifyOTPResponse(sentOTP.getId());
    }

    public UserEntity getLoggedInUser() {
        Long userId = jwtUtils.getClientId();
        return userRepository.findByIdAndDbStatus(userId, DbStatus.ACTIVE)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found logged user in database"));
    }
}
