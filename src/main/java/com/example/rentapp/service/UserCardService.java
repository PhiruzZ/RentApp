package com.example.rentapp.service;

import com.example.rentapp.model.entity.Transaction;
import com.example.rentapp.model.entity.UserCard;
import com.example.rentapp.repository.UserCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCardService {

    private final UserCardRepository userCardRepository;

    public void createUserCard(Transaction transaction) {
        UserCard userCard = new UserCard();
        userCard.setProviderTransactionId(transaction.getProviderTransactionId());
        userCard.setPaymentProvider(transaction.getPaymentProvider());
        userCardRepository.save(userCard);
    }


}
