package com.example.rentapp.service;

import com.example.rentapp.model.entity.UserBalance;
import com.example.rentapp.model.entity.UserEntity;
import com.example.rentapp.model.enums.Currency;
import com.example.rentapp.repository.UserBalanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserBalanceService {

    private final UserBalanceRepository userBalanceRepository;


    public UserBalance createDefaultBalance(UserEntity user) {
        UserBalance userBalance = new UserBalance();
        userBalance.setUser(user);
        userBalance.setAvailableAmount(0.0);
        userBalance.setBlockedAmount(0.0);
        userBalance.setCurrency(Currency.GEL);
        return userBalance;
    }
}
