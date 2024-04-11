package com.example.rentapp.service;

import com.example.rentapp.model.dto.AgreementRequestDto;
import com.example.rentapp.model.entity.AgreementRequest;
import com.example.rentapp.model.entity.Product;
import com.example.rentapp.model.entity.UserEntity;
import com.example.rentapp.model.enums.AgreementRequestStatus;
import com.example.rentapp.model.enums.DbStatus;
import com.example.rentapp.model.enums.ProductStatus;
import com.example.rentapp.model.enums.TransactionType;
import com.example.rentapp.model.request.CreateAgreementRequestRequest;
import com.example.rentapp.repository.AgreementRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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
        Product product = productService.findByIdAndProductStatus(request.getProductId(), ProductStatus.AVAILABLE);
        if(product.getOwner().getId().equals(user.getId())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "You can't rent your product");
        }
        if(request.getFromDate().isBefore(product.getAvailableFrom()) || request.getToDate().isAfter(product.getAvailableUntil())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Requested dates is not valid for product!");
        }
        if(request.getToDate().isBefore(request.getFromDate())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid dates");
        }
        AgreementRequest agreementRequest = createAgreementRequest(request, product, user);
        if(product.getAdvancePaymentPercent() != null && product.getAdvancePaymentPercent() > 0.0){
            Double amount = productService.calcPriceForDates(product.getProductPrice(),request.getFromDate(), request.getToDate());
            Double transferAmount = amount * product.getAdvancePaymentPercent() / 100.0;
            transactionService.makeProductPayment(user.getUserBalance().getId(), product.getOwner().getUserBalance().getId(), transferAmount, agreementRequest, TransactionType.ADVANCE_PAYMENT);
        }

        String emailText = "You have request on product with name: " + product.getName();
        emailService.sendMail(emailText, product.getOwner().getEmail());
    }

    private AgreementRequest createAgreementRequest(CreateAgreementRequestRequest request, Product product, UserEntity user) {
        AgreementRequest agreementRequest = new AgreementRequest();
        agreementRequest.setStatus(AgreementRequestStatus.PENDING);
        agreementRequest.setProduct(product);
        agreementRequest.setFromUser(user);
        agreementRequest.setFromDate(request.getFromDate());
        agreementRequest.setToDate(request.getToDate());
        return agreementRequestRepository.save(agreementRequest);
    }


    public AgreementRequest findByIdAndUserId(Long id, Long userId) {
        return agreementRequestRepository.findByIdAndDbStatusAndUserId(id, DbStatus.ACTIVE, userId)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private AgreementRequest findByIdAndFromUserId(Long id, Long userId) {
        return agreementRequestRepository.findByIdAndDbStatusAndFromUserId(id, DbStatus.ACTIVE, userId)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Transactional
    public void cancel(Long id) {
        UserEntity user = authService.getLoggedInUser();
        AgreementRequest request = findByIdAndFromUserId(id, user.getId());
        if(request.getStatus().equals(AgreementRequestStatus.CANCELLED)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Request is already " + request.getStatus().name().toLowerCase());
        }
        if(!request.getStatus().equals(AgreementRequestStatus.PENDING)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "You can't cancel request with status: " + request.getStatus());
        }
        if(request.getProduct().getAdvancePaymentPercent() != null
                && request.getProduct().getAdvancePaymentPercent() > 0.0){
            transactionService.refundTransactionForAgreementRequest(request.getId());
        }
        request.setStatus(AgreementRequestStatus.CANCELLED);
        request.getProduct().setProductStatus(ProductStatus.AVAILABLE);
        agreementRequestRepository.save(request);
        productService.save(request.getProduct());
    }


    @Transactional
    public void finalise(Long id) {
        UserEntity user = authService.getLoggedInUser();
        AgreementRequest request = findByIdAndFromUserId(id, user.getId());

        if (request.getStatus().equals(AgreementRequestStatus.FINALISED)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Already finalised");
        }
        if(!request.getStatus().equals(AgreementRequestStatus.APPROVED)){
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Not approved yet");
        }
        request.setStatus(AgreementRequestStatus.FINALISED);
        Double price = productService.calcPriceForDates(request.getProduct().getProductPrice(), request.getFromDate(), request.getToDate());
        Double transferAmount = price * (100 - request.getProduct().getAdvancePaymentPercent()) / 100.0;
        transactionService.makeProductPayment(user.getUserBalance().getId(),request.getProduct().getOwner().getUserBalance().getId(), transferAmount, request, TransactionType.FINAL_PAYMENT);
        transactionService.moveAmountFromBlockedToAvailable(request.getProduct().getOwner().getUserBalance(), price);
        agreementRequestRepository.save(request);
    }

    @Transactional
    public void approve(Long id) {
        UserEntity user = authService.getLoggedInUser();
        AgreementRequest request = findByIdAndProductOwnerIdUserId(id, user.getId());
        if(!request.getStatus().equals(AgreementRequestStatus.PENDING)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Request is already " + request.getStatus().name().toLowerCase());
        }
        request.setStatus(AgreementRequestStatus.APPROVED);
        request.getProduct().setProductStatus(ProductStatus.NOT_AVAILABLE);
        productService.save(request.getProduct());
        agreementRequestRepository.save(request);
        if(request.getStatus().equals(AgreementRequestStatus.APPROVED)){
            List<AgreementRequest> allRequestsForProduct = agreementRequestRepository.findByProductIdAndDbStatusAndStatus(request.getProduct().getId(), DbStatus.ACTIVE, AgreementRequestStatus.PENDING);
            allRequestsForProduct = allRequestsForProduct.stream().peek(agreementRequest -> {
                agreementRequest.setStatus(AgreementRequestStatus.REJECTED);
                if(agreementRequest.getProduct().getAdvancePaymentPercent() != null
                        && agreementRequest.getProduct().getAdvancePaymentPercent() > 0.0) {
                    transactionService.refundTransactionForAgreementRequest(agreementRequest.getId());
                }
            }).toList();
            agreementRequestRepository.saveAll(allRequestsForProduct);
        }
    }

    @Transactional
    public void reject(Long id) {
        UserEntity user = authService.getLoggedInUser();
        AgreementRequest request = findByIdAndProductOwnerIdUserId(id, user.getId());
        if(!request.getStatus().equals(AgreementRequestStatus.PENDING)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Request is already " + request.getStatus().name().toLowerCase());
        }
        request.setStatus(AgreementRequestStatus.REJECTED);
        agreementRequestRepository.save(request);
        if(request.getProduct().getAdvancePaymentPercent() != null
                && request.getProduct().getAdvancePaymentPercent() > 0.0){
            transactionService.refundTransactionForAgreementRequest(request.getId());
        }
    }

    private AgreementRequest findByIdAndProductOwnerIdUserId(Long id, Long ownerId) {
        return agreementRequestRepository.findByIdAndOwnerIdAndDbStatus(id, ownerId, DbStatus.ACTIVE)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public List<AgreementRequestDto> findByProduct(Long productId) {
        UserEntity user = authService.getLoggedInUser();
        Product product = productService.findByIdAndDbStatusAndOwnerId(productId, DbStatus.ACTIVE, user.getId());
        List<AgreementRequest> agreementRequests = agreementRequestRepository.findByProductIdAndDbStatus(product.getId(), DbStatus.ACTIVE);
        return AgreementRequestDto.listOf(agreementRequests);
    }

    public List<AgreementRequestDto> getByUser() {
        UserEntity user = authService.getLoggedInUser();
        List<AgreementRequest> agreementRequests = agreementRequestRepository.findByFromUserIdAndDbStatus(user.getId(), DbStatus.ACTIVE);
        return AgreementRequestDto.listOf(agreementRequests);
    }
}
