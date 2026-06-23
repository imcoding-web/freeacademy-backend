package fr.imcoding.edu365.business.services.payment;

import fr.imcoding.edu365.business.services.SkillAreaPackageService;
import fr.imcoding.edu365.business.services.UserService;
import fr.imcoding.edu365.enumeration.PackageType;
import fr.imcoding.edu365.persistence.entities.InformationSeeker;
import fr.imcoding.edu365.persistence.entities.SkillAreaPackage;
import org.springframework.stereotype.Service;

import fr.imcoding.edu365.business.services.OfferService;
import fr.imcoding.edu365.business.services.payment.providers.PaymeePaymentService;
import fr.imcoding.edu365.business.services.payment.providers.SobflousPaymentService;
import fr.imcoding.edu365.dtos.ext.TransationOrder;
import fr.imcoding.edu365.enumeration.OfferStatus;
import fr.imcoding.edu365.enumeration.PaymentType;
import fr.imcoding.edu365.persistence.entities.Offer;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Service
@RequiredArgsConstructor
public class ExtPaymentService {

	private final SobflousPaymentService sobflousPaymentService;
	private final PaymeePaymentService paymeePaymentService;
	private final OfferService offerService;
	private final UserService userService;
	private final SkillAreaPackageService packageService;
	
	public TransationOrder getRedirectionPaymentUrl(PaymentType paymentType, String orderId) {
		Offer offer = offerService.getOfferById(orderId);
		if(offer != null && offer.getOfferStatus() == OfferStatus.PENDING_CUSTOMER_PAYMENT) {
			if(paymentType.equals(PaymentType.SOBFLOUS)){
				return sobflousPaymentService.getRedirectionPaymentUrl(orderId);
			}else if(paymentType.equals(PaymentType.PAYMEE)) {
				return paymeePaymentService.getRedirectionPaymentUrl(offer.getOfferPriceToPay(), offer.getUniqueIdentifier());
			}
			else
				return null;
		}
		return null;
		
	}

	public TransationOrder getRedirectionPaymentUrlToPayPackage(PaymentType paymentType, PackageType packageType) throws Exception {
		//Triater uniquement le paymentType: Paymee
		InformationSeeker student = (InformationSeeker) userService.getCurrentUser();
		if(student == null) throw new Exception("L'étudiant est non connecté");
		if (student.getCurrentLevel() == null) throw new Exception("Le niveau educatif de l'étudiant est non renseigné");
		SkillAreaPackage pack = packageService.getPackBySkillLevelAndType(student.getCurrentLevel().getUuid(), packageType);
		String orderIdentifier = student.getUniqueIdentifier() + "-" + getToday();
		TransationOrder transactionOrder = paymeePaymentService.getRedirectionPaymentUrl(pack.getPackagePrice(), orderIdentifier);
		paymeePaymentService.savePaymeeTransaction(null, pack, transactionOrder.getToken());
		return transactionOrder;
	}

	private String getToday() {
		LocalDate today = LocalDate.now();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return today.format(format);

	}
}
