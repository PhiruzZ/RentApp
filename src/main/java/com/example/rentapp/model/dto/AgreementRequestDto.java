package com.example.rentapp.model.dto;

import com.example.rentapp.model.entity.AgreementRequest;
import com.example.rentapp.model.enums.AgreementRequestStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@Builder
public class AgreementRequestDto {

    private Long id;
    private Long fromUserId;
    private Long productId;
    private LocalDate fromDate;
    private LocalDate toDate;
    private AgreementRequestStatus status;

    public static List<AgreementRequestDto> listOf(List<AgreementRequest> agreementRequests) {
        return agreementRequests.stream()
                .map(agreementRequest -> AgreementRequestDto.builder()
                        .id(agreementRequest.getId())
                        .fromUserId(agreementRequest.getFromUser().getId())
                        .productId(agreementRequest.getProduct().getId())
                        .fromDate(agreementRequest.getFromDate())
                        .toDate(agreementRequest.getToDate())
                        .status(agreementRequest.getStatus())
                        .build()
                ).toList();
    }
}
