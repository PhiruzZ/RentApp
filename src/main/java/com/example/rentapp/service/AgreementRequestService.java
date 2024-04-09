package com.example.rentapp.service;

import com.example.rentapp.model.entity.AgreementRequest;
import com.example.rentapp.model.entity.Product;
import com.example.rentapp.model.entity.UserEntity;
import com.example.rentapp.model.enums.AgreementRequestStatus;
import com.example.rentapp.model.enums.DbStatus;
import com.example.rentapp.model.enums.TransactionType;
import com.example.rentapp.model.request.CreateAgreementRequestRequest;
import com.example.rentapp.repository.AgreementRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AgreementRequestService {

    private final AgreementRequestRepository agreementRequestRepository;
    private final ProductService productService;
    private final AuthService authService;
    private final EmailService emailService;
    private final TransactionService transactionService;

    @Transactional
    public void create(CreateAgreementRequestRequest request) {
        UserEntity user = authService.getLoggedInUser();
        Product product = productService.findById(request.getProductId());
        Optional<AgreementRequest> approvedRequest = agreementRequestRepository.findApprovedRequestForProduct(product.getId(), AgreementRequestStatus.APPROVED, DbStatus.ACTIVE);
        if(approvedRequest.isPresent()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Already approved request exists for this product");
        }
        if(product.getAdvancePaymentPercent() != null && product.getAdvancePaymentPercent() > 0.0){
            Double amount = productService.calcPriceForDates(product.getProductPrice(),request.getFromDate(), request.getToDate());
            Double transferAmount = amount * product.getAdvancePaymentPercent() / 100.0;
            transactionService.makeProductPayment(user.getUserBalance(), product.getOwner().getUserBalance(), transferAmount, product, TransactionType.ADVANCE_PAYMENT);
        }
        createAgreementRequest(request, product, user);

        String emailText = "You have request on product with name: " + product.getName();
        emailService.sendMail(emailText, product.getOwner().getEmail());
    }

    private void createAgreementRequest(CreateAgreementRequestRequest request, Product product, UserEntity user) {
        AgreementRequest agreementRequest = new AgreementRequest();
        agreementRequest.setStatus(AgreementRequestStatus.PENDING);
        agreementRequest.setProduct(product);
        agreementRequest.setFromUser(user);
        agreementRequest.setFromDate(request.getFromDate());
        agreementRequest.setToDate(request.getToDate());
        agreementRequestRepository.save(agreementRequest);
    }

    @Transactional
    public void setStatus(Long id, AgreementRequestStatus agreementRequestStatus) {
        UserEntity user = authService.getLoggedInUser();
        AgreementRequest request = findByIdAndUserId(id, user.getId());
        if(!request.getStatus().equals(AgreementRequestStatus.PENDING)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Request is already " + request.getStatus().name().toLowerCase());
        }
        request.setStatus(agreementRequestStatus);
        agreementRequestRepository.save(request);
        if(request.getStatus().equals(AgreementRequestStatus.APPROVED)){
            List<AgreementRequest> allRequestsForProduct = agreementRequestRepository.findByProductIdAndDbStatusAndStatus(request.getProduct().getId(), DbStatus.ACTIVE, AgreementRequestStatus.PENDING);
            allRequestsForProduct = allRequestsForProduct.stream().peek((agreementRequest -> agreementRequest.setStatus(AgreementRequestStatus.REJECTED))).toList();
            agreementRequestRepository.saveAll(allRequestsForProduct);
        }
    }

    private AgreementRequest findByIdAndUserId(Long id, Long userId) {
        return agreementRequestRepository.findByIdAndDbStatusAndUserId(id, DbStatus.ACTIVE, userId)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private AgreementRequest findByIdAndFromUserId(Long id, Long userId) {
        return agreementRequestRepository.findByIdAndDbStatusAndFromUserId(id, DbStatus.ACTIVE, userId)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public void cancel(Long id) {
        UserEntity user = authService.getLoggedInUser();
        AgreementRequest request = findByIdAndUserId(id, user.getId());
        if(request.getStatus().equals(AgreementRequestStatus.CANCELLED)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Request is already " + request.getStatus().name().toLowerCase());
        }
        if(request.getStatus().equals(AgreementRequestStatus.APPROVED)){
            if(user.getId().equals(request.getProduct().getOwner().getId())){
                //TODO: make refund of advance payment
            }
        }
    }


    public void finalise(Long id) {
        UserEntity user = authService.getLoggedInUser();
        AgreementRequest request = findByIdAndFromUserId(id, user.getId());
        if(!request.getStatus().equals(AgreementRequestStatus.APPROVED)){
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Not approved yet");
        }
        if (!request.getStatus().equals(AgreementRequestStatus.FINALISED)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Already finalised");
        }
        request.setStatus(AgreementRequestStatus.FINALISED);
        Double price = productService.calcPriceForDates(request.getProduct().getProductPrice(), request.getFromDate(), request.getToDate());
        Double transferAmount = price * (100 - request.getProduct().getAdvancePaymentPercent()) / 100.0;
        transactionService.makeProductPayment(user.getUserBalance(),request.getProduct().getOwner().getUserBalance(), transferAmount, request.getProduct(), TransactionType.FINAL_PAYMENT);
        agreementRequestRepository.save(request);
    }
}
