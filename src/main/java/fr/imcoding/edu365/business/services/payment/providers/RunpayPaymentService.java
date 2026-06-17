package fr.imcoding.edu365.business.services.payment.providers;

import org.springframework.stereotype.Service;

import fr.imcoding.edu365.business.ext.runpay.response.RunpayOrderInfoCustomerResponse;
import fr.imcoding.edu365.business.ext.runpay.response.RunpayOrderInfoDataResponse;
import fr.imcoding.edu365.business.ext.runpay.response.RunpayOrderInfoResponse;
import fr.imcoding.edu365.business.ext.runpay.response.RunpaySetOrderStatusResponse;
import fr.imcoding.edu365.business.services.OfferService;
import fr.imcoding.edu365.business.services.PaymentService;
import fr.imcoding.edu365.enumeration.OfferStatus;
import fr.imcoding.edu365.enumeration.PaymentType;
import fr.imcoding.edu365.persistence.entities.Offer;
import fr.imcoding.edu365.utils.Utils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RunpayPaymentService {
	private final OfferService offerService;
	private final PaymentService paymentService;

	public RunpayOrderInfoResponse getRunpayOrderInfo(String orderId) {
		Offer offer = offerService.getOfferById(orderId);
		if (offer != null) {
			if (offer.getOfferStatus() != OfferStatus.VALIDATED_AND_PAID) {
				return RunpayOrderInfoResponse.builder().code("200").message("Votre commande a été bien trouvé!")
						.data(RunpayOrderInfoDataResponse.builder().amount(Utils.convertAmountToStringWithSeperator(offer.getOfferPriceToPay()))
								.id(offer.getUniqueIdentifier())
								.customer(RunpayOrderInfoCustomerResponse.builder()
										.name(offer.getAnnouncement().getAnnouncementPublisher().getUserFirstName()
												+ " "+ offer.getAnnouncement().getAnnouncementPublisher().getUserLastName())
										.order(offer.getAnnouncement().getAnnouncementTitle()).build())

								.build())
						.build();
			} else {
				return RunpayOrderInfoResponse.builder().code("A001").message("Commande déjà payé!")
						.data(RunpayOrderInfoDataResponse.builder().amount(Utils.convertAmountToStringWithSeperator(offer.getOfferPriceToPay()))
								.id(offer.getUniqueIdentifier())
								.customer(RunpayOrderInfoCustomerResponse.builder()
										.name(offer.getAnnouncement().getAnnouncementPublisher().getUserFirstName()
												+ offer.getAnnouncement().getAnnouncementPublisher().getUserLastName())
										.order(offer.getAnnouncement().getAnnouncementTitle()).build())

								.build())
						.build();
			}

		}
		return RunpayOrderInfoResponse.builder().code("A001").message("Commande non existant!").build();

	}
	
	public RunpaySetOrderStatusResponse setRunpayOrderStatus(String orderId, String amount) {
		Offer offer = offerService.getOfferById(orderId);
		String offerAmount = Utils.convertAmountToStringWithSeperator(offer.getOfferPriceToPay());
		if (offerAmount.equals(amount)) {
			if (offer.getOfferStatus() != OfferStatus.VALIDATED_AND_PAID) {
				paymentService.savePaymentOperation(offer, PaymentType.RUNPAY_TERMINAL);
				return RunpaySetOrderStatusResponse.builder().code("200")
						.message("Commande payée avec succès et merci pour votre confiance en Free Academy").build();
			} else {
				return RunpaySetOrderStatusResponse.builder().code("A002")
						.message("Commande déjà payé!").build();
			}
			
		}
		return RunpaySetOrderStatusResponse.builder().code("A002")
				.message("Montant non conforme! Merci de vérifier le montant de votre commande.").build();
	}

}
