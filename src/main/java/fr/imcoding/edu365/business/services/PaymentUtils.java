package fr.imcoding.edu365.business.services;

import fr.imcoding.edu365.business.mappers.MediaDatailsMapper;
import fr.imcoding.edu365.dtos.MediaDetails;
import fr.imcoding.edu365.persistence.entities.Payment;
import fr.imcoding.edu365.persistence.entities.UserFees;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Rokaya
 * @Date 17/10/2022
 */
@Component
@RequiredArgsConstructor
public class PaymentUtils {
  private final MediaDatailsMapper mediaDatailsMapper;

  public MediaDetails getPaymentMedia(Payment payment){
    return payment.getPaymentMedia()!= null ? mediaDatailsMapper
        .toMediaDetails(payment.getPaymentMedia())
        :null;

  }

  public MediaDetails getUserFeePaymentMedia(UserFees payment){
    return payment.getPaymentMedia()!= null ? mediaDatailsMapper
        .toMediaDetails(payment.getPaymentMedia())
        :null;

  }
}
