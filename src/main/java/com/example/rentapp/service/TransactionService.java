package com.example.rentapp.service;

import com.example.rentapp.model.dto.TransactionDto;
import com.example.rentapp.model.entity.Product;
import com.example.rentapp.model.entity.Transaction;
import com.example.rentapp.model.entity.UserBalance;
import com.example.rentapp.model.entity.UserEntity;
import com.example.rentapp.model.enums.DbStatus;
import com.example.rentapp.model.enums.PaymentProvider;
import com.example.rentapp.model.enums.TransactionStatus;
import com.example.rentapp.model.enums.TransactionType;
import com.example.rentapp.model.request.TransactionRequest;
import com.example.rentapp.repository.TransactionRepository;
import com.example.rentapp.repository.UserBalanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AuthService authService;
    private final UserBalanceRepository userBalanceRepository;
    @Transactional
    public void topUp(TransactionRequest request) {
        UserEntity user = authService.getLoggedInUser();
        UserBalance balance = user.getUserBalance();
        balance.setAvailableAmount(balance.getAvailableAmount() + request.getAmount());
        userBalanceRepository.save(balance);
        createTransaction(null, balance, request.getAmount(),
                null, TransactionType.TOPUP, PaymentProvider.MOCK_ACQUIRING_PROVIDER, UUID.randomUUID().toString());
    }

    public void makeProductPayment(UserBalance fromBalance, UserBalance toBalance,
                                   Double transferAmount, Product product, TransactionType transactionType) {
        if(fromBalance.getAvailableAmount() < transferAmount){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You don't have enough balance");
        }
        fromBalance.setAvailableAmount(fromBalance.getAvailableAmount() - transferAmount);
        toBalance.setBlockedAmount(toBalance.getBlockedAmount() + transferAmount);
        userBalanceRepository.save(fromBalance);
        userBalanceRepository.save(toBalance);
        createTransaction(fromBalance, toBalance, transferAmount, product, transactionType,
                null, null);
    }

    private void createTransaction(UserBalance fromBalance, UserBalance toBalance,
                                   Double transferAmount, Product product,
                                   TransactionType transactionType, PaymentProvider paymentProvider,
                                   String providerTransactionId) {
        Transaction transaction = new Transaction();
        transaction.setProduct(product);
        transaction.setTransactionStatus(TransactionStatus.SUCCESS);
        transaction.setAmount(transferAmount);
        transaction.setFromBalance(fromBalance);
        transaction.setToBalance(toBalance);
        transaction.setTransactionType(transactionType);
        transaction.setPaymentProvider(paymentProvider);
        transaction.setProviderTransactionId(providerTransactionId);
        transactionRepository.save(transaction);
    }

    public void cashOut(TransactionRequest request) {
        UserEntity user = authService.getLoggedInUser();
        UserBalance balance = user.getUserBalance();
        balance.setAvailableAmount(balance.getAvailableAmount() - request.getAmount());
        userBalanceRepository.save(balance);
        createTransaction(balance, null, request.getAmount(),
                null, TransactionType.CASHOUT, PaymentProvider.MOCK_DISTRIBUTOR_PROVIDER, UUID.randomUUID().toString());

    }

    public List<TransactionDto> getAll() {
        UserEntity user = authService.getLoggedInUser();
        List<Transaction> transactions = transactionRepository.findByUserIdAndDbStatus(user.getId(), DbStatus.ACTIVE);
        return TransactionDto.listOf(transactions);
    }
}
