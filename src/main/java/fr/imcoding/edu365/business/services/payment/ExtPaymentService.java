package fr.imcoding.edu365.business.services.payment;

import org.springframework.stereotype.Service;

import fr.imcoding.edu365.business.services.OfferService;
import fr.imcoding.edu365.business.services.payment.providers.PaymeePaymentService;
import fr.imcoding.edu365.business.services.payment.providers.SobflousPaymentService;
import fr.imcoding.edu365.dtos.ext.TransationOrder;
import fr.imcoding.edu365.enumeration.OfferStatus;
import fr.imcoding.edu365.enumeration.PaymentType;
import fr.imcoding.edu365.persistence.entities.Offer;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExtPaymentService {

	private final SobflousPaymentService sobflousPaymentService;
	private final PaymeePaymentService paymeePaymentService;
	private final OfferService offerService;
	
	public TransationOrder getRedirectionPaymentUrl(PaymentType paymentType, String orderId) {
		Offer offer = offerService.getOfferById(orderId);
		if(offer != null && offer.getOfferStatus() == OfferStatus.PENDING_CUSTOMER_PAYMENT) {
			if(paymentType.equals(PaymentType.SOBFLOUS)){
				return sobflousPaymentService.getRedirectionPaymentUrl(orderId);
			}else if(paymentType.equals(PaymentType.PAYMEE)) {
				return paymeePaymentService.getRedirectionPaymentUrl(orderId);
			}
			else
				return null;
		}
		return null;
		
	}
}
