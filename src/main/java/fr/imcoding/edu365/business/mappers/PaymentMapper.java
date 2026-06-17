package fr.imcoding.edu365.business.mappers;

import fr.imcoding.edu365.business.services.PaymentRequestService;
import fr.imcoding.edu365.business.services.PaymentUtils;
import fr.imcoding.edu365.business.services.UserBankDataService;
import fr.imcoding.edu365.client.dtos.response.ExpertPaymentResponse;
import fr.imcoding.edu365.client.dtos.response.PaymentResponse;
import fr.imcoding.edu365.dtos.UserDetails;
import fr.imcoding.edu365.persistence.entities.InformationGiver;
import fr.imcoding.edu365.persistence.entities.Payment;
import fr.imcoding.edu365.persistence.entities.UserBankData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Rokaya
 * @Date 16/10/2022
 */
@Component
@RequiredArgsConstructor
public class PaymentMapper {
  private final OfferMapper offerMapper;
  private final PaymentUtils paymentUtils;
  private final UserMapper userMapper;
  private final UserBankDataService userBankDataService;
  private final PaymentRequestService paymentRequestService;



  public PaymentResponse toPaymentResponse(Payment payment){
    return new PaymentResponse(payment.getUuid(),payment.getPaymentStatus(),
        payment.getPaymentType(),
        offerMapper.toOfferPaymentResponseDto(offerMapper.toOfferResponseDto(payment.getOffer())),payment.getCreatedAt(),payment.getRefusalReason(),paymentUtils.getPaymentMedia(payment)
            );
  }

  public ExpertPaymentResponse toExpertPaymentResponse(InformationGiver expert){
    UserDetails userDetails = userMapper.toUserDetailsResponse(expert);
    UserBankData bankDataResponse=userBankDataService.getUserBankData(expert.getUuid());

    return new ExpertPaymentResponse(
        expert.getUuid(),
        expert.getUserFirstName().concat(" ").concat(expert.getUserLastName()),
        userDetails.getUserProfilePicture(),bankDataResponse.getRib(),
        bankDataResponse.getAccumulatedBalance(),
        bankDataResponse.getUnpaidAccumulatedBalance(),
        paymentRequestService.getByExpertUuid(expert.getUuid()));
  }

}
