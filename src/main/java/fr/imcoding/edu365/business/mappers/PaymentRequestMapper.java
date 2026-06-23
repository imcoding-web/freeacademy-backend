package fr.imcoding.edu365.business.mappers;

import fr.imcoding.edu365.client.dtos.request.PaymentRequestDto;
import fr.imcoding.edu365.client.dtos.response.PaymentRequestResponse;
import fr.imcoding.edu365.persistence.entities.PaymentRequest;
import fr.imcoding.edu365.persistence.entities.UserBankData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Rokaya
 * @Date 25/12/2022
 */
@Component
@RequiredArgsConstructor
public class PaymentRequestMapper {

  public PaymentRequestDto toPaymentRequestDto(PaymentRequest paymentRequest) {
    return PaymentRequestDto.builder().amount(paymentRequest.getAmount()).transferCreatedDate(
        paymentRequest.getCreatedAt()).build();

  }

  public PaymentRequestResponse toPaymentRequestResponse(PaymentRequest paymentRequest) {
    return PaymentRequestResponse.builder().transfertUuid(paymentRequest.getUuid()).expertFullname(
        paymentRequest.getExpert().getUserFirstName().concat(" ").concat(paymentRequest.getExpert().getUserLastName())).amount(
        paymentRequest.getAmount()).transferCreatedDate(paymentRequest.getCreatedAt()).build();

  }
  public PaymentRequestResponse toPaymentRequestValidationResponse(PaymentRequest paymentRequest,UserBankData userBankData) {
    return PaymentRequestResponse.builder()
        .transfertUuid(paymentRequest.getUuid())
        .expertUuid(paymentRequest.getExpert().getUuid())
        .expertFullname(paymentRequest
            .getExpert().getUserFirstName().concat(" ").concat(paymentRequest.getExpert().getUserLastName()))
        .amount(paymentRequest.getAmount()).accumulatedBalance(userBankData.getAccumulatedBalance())
        .unpaidAccumulatedBalance(userBankData.getUnpaidAccumulatedBalance())
        .transferCreatedDate(paymentRequest.getCreatedAt())
        .build();

  }

}
