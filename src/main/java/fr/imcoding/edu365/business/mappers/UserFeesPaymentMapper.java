package fr.imcoding.edu365.business.mappers;

import fr.imcoding.edu365.business.services.PaymentUtils;
import fr.imcoding.edu365.client.dtos.response.PaymentFeesDetails;
import fr.imcoding.edu365.client.dtos.response.PaymentFeesResponse;
import fr.imcoding.edu365.client.dtos.response.PaymentResponse;
import fr.imcoding.edu365.client.dtos.response.UserFeesPaymenrResponse;
import fr.imcoding.edu365.persistence.entities.Payment;
import fr.imcoding.edu365.persistence.entities.UserFees;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Rokaya
 * @Date 27/01/2023
 */
@Component
@RequiredArgsConstructor
public class UserFeesPaymentMapper {
  private final PaymentUtils paymentUtils;
  private final ExpertOfferDetailsMapper expertOfferDetailsMapper;


  public UserFeesPaymenrResponse toPaymentResponse(UserFees payment,boolean isExemptFromFees){
    return new UserFeesPaymenrResponse( payment!=null?new PaymentFeesDetails(payment.getUuid(),payment.getPaymentStatus(),
        payment.getPaymentType(),
        payment.getCreatedAt(),payment.getRefusalReason(),paymentUtils.getUserFeePaymentMedia(payment)):null,isExemptFromFees
    );
  }
  public PaymentFeesResponse toPaymentFeeResponse(UserFees payment){
    return new PaymentFeesResponse(
        payment.getUuid(),
        payment.getPaymentStatus(),
        payment.getPaymentType(),
        payment.getFeesType(),
        expertOfferDetailsMapper.toExpertDetailsForOffer(payment.getUser()),
        payment.getCreatedAt(),
        payment.getRefusalReason(),
        paymentUtils.getUserFeePaymentMedia(payment)
    );
  }
}
