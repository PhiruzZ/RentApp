package com.example.rentapp.service;

import com.example.rentapp.model.dto.TransactionDto;
import com.example.rentapp.model.entity.AgreementRequest;
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
    private final UserCardService userCardService;

    @Transactional
    public void topUp(TransactionRequest request) {
        UserEntity user = authService.getLoggedInUser();
        UserBalance balance = user.getUserBalance();
        balance.setAvailableAmount(balance.getAvailableAmount() + request.getAmount());
        userBalanceRepository.save(balance);
        Transaction transaction = createTransaction(null, balance, request.getAmount(),
                null, TransactionType.TOPUP, PaymentProvider.MOCK_ACQUIRING_PROVIDER, UUID.randomUUID().toString());
        if(request.isSaveCard()){
            userCardService.createUserCard(transaction);
        }
    }

    public UserBalance getBalanceByIdAndAcquireLock(Long balanceId){
        return userBalanceRepository.findByIdAndDbStatusAndLock(balanceId, DbStatus.ACTIVE)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public UserBalance getBalanceById(Long balanceId){
        return userBalanceRepository.findByIdAndDbStatus(balanceId, DbStatus.ACTIVE)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public void makeProductPayment(Long fromBalanceId, Long toBalanceId,
                                   Double transferAmount, AgreementRequest agreementRequest, TransactionType transactionType) {
        UserBalance fromBalance = getBalanceByIdAndAcquireLock(fromBalanceId);
        UserBalance toBalance = getBalanceById(toBalanceId);
        if(fromBalance.getAvailableAmount() < transferAmount){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You don't have enough balance");
        }
        fromBalance.setAvailableAmount(fromBalance.getAvailableAmount() - transferAmount);
        toBalance.setBlockedAmount(toBalance.getBlockedAmount() + transferAmount);
        userBalanceRepository.save(fromBalance);
        userBalanceRepository.save(toBalance);
        createTransaction(fromBalance, toBalance, transferAmount, agreementRequest, transactionType,
                null, null);
    }

    private Transaction createTransaction(UserBalance fromBalance, UserBalance toBalance,
                                   Double transferAmount, AgreementRequest agreementRequest,
                                   TransactionType transactionType, PaymentProvider paymentProvider,
                                   String providerTransactionId) {
        Transaction transaction = new Transaction();
        transaction.setAgreementRequest(agreementRequest);
        transaction.setTransactionStatus(TransactionStatus.SUCCESS);
        transaction.setAmount(transferAmount);
        transaction.setFromBalance(fromBalance);
        transaction.setToBalance(toBalance);
        transaction.setTransactionType(transactionType);
        transaction.setPaymentProvider(paymentProvider);
        transaction.setProviderTransactionId(providerTransactionId);
        return transactionRepository.save(transaction);
    }

    public void cashOut(TransactionRequest request) {
        UserEntity user = authService.getLoggedInUser();
        UserBalance balance = getBalanceByIdAndAcquireLock(user.getUserBalance().getId());
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

    public void refundTransactionForAgreementRequest(Long agreementRequestId) {
        Transaction transaction = transactionRepository.findByAgreementRequestIdAndDbStatus(agreementRequestId, DbStatus.ACTIVE)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Not fount transaction for agreement request"));
        if(!transaction.getTransactionStatus().equals(TransactionStatus.SUCCESS)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can't refund transaction with status " + transaction.getTransactionStatus());
        }
        UserBalance fromBalance = getBalanceById(transaction.getFromBalance().getId());
        UserBalance toBalance = getBalanceByIdAndAcquireLock(transaction.getToBalance().getId());

        fromBalance.setAvailableAmount(fromBalance.getAvailableAmount() + transaction.getAmount());
        toBalance.setBlockedAmount(toBalance.getBlockedAmount() - transaction.getAmount());
        transaction.setTransactionStatus(TransactionStatus.REFUNDED);
        transactionRepository.save(transaction);
        userBalanceRepository.save(fromBalance);
        userBalanceRepository.save(toBalance);
    }

    public void moveAmountFromBlockedToAvailable(UserBalance userBalance, Double price) {
        userBalance.setBlockedAmount(userBalance.getBlockedAmount() - price);
        userBalance.setAvailableAmount(userBalance.getAvailableAmount() + price);
        userBalanceRepository.save(userBalance);
    }
}
