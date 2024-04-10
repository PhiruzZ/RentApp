package com.example.rentapp.model.dto;

import com.example.rentapp.model.entity.UserEntity;
import com.example.rentapp.model.enums.VerificationStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class UserDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private VerificationStatus verificationStatus;
    private Double availableBalance;
    private Double blockedBalance;

    public static UserDto of(UserEntity user) {
        return UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .verificationStatus(user.getVerificationStatus())
                .availableBalance(user.getUserBalance().getAvailableAmount())
                .blockedBalance(user.getUserBalance().getBlockedAmount())
                .build();
    }

    public static List<UserDto> listOf(List<UserEntity> userEntities) {
        return userEntities.stream()
                .map(UserDto::of)
                .toList();
    }
}
