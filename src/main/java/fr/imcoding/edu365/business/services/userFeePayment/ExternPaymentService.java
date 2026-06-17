package fr.imcoding.edu365.business.services.userFeePayment;

import fr.imcoding.edu365.business.services.UserFeesService;
import fr.imcoding.edu365.business.services.UserService;
import fr.imcoding.edu365.business.services.userFeePayment.providers.PaymeeFeesPaymentService;
import fr.imcoding.edu365.business.services.userFeePayment.providers.SobflousFeesPaymentService;
import fr.imcoding.edu365.dtos.ext.TransationOrder;
import fr.imcoding.edu365.enumeration.FeesType;
import fr.imcoding.edu365.enumeration.PaymentType;
import fr.imcoding.edu365.persistence.entities.InformationGiver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExternPaymentService {

	private final SobflousFeesPaymentService sobflousFeesPaymentService;
	private final PaymeeFeesPaymentService paymeeFeesPaymentService;
	private final UserService userService;
	private final UserFeesService userFeesService;


	public TransationOrder getRedirectionPaymentUrl(PaymentType paymentType) {
		InformationGiver user = (InformationGiver) userService.getCurrentUser();
		if (userFeesService.getUserFeesByUserUuid(user.getUuid(),FeesType.REGISTRATION)==null) {
			if(paymentType.equals(PaymentType.SOBFLOUS)){
				return sobflousFeesPaymentService.getRedirectionPaymentUrl();
			}else if(paymentType.equals(PaymentType.PAYMEE)) {
				return paymeeFeesPaymentService.getRedirectionPaymentUrl();
			}
			else
				return null;
		}
		return null;
		
	}
}
