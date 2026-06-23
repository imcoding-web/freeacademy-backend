package fr.imcoding.edu365.business.services.userFeePayment.providers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.imcoding.edu365.business.ext.paymee.response.PaymeeCheckPaymentResponse;
import fr.imcoding.edu365.business.ext.paymee.response.PaymeeResponse;
import fr.imcoding.edu365.business.services.PaymeeInprogressTransactionsService;
import fr.imcoding.edu365.business.services.PaymentService;
import fr.imcoding.edu365.business.services.UserFeesService;
import fr.imcoding.edu365.dtos.ext.TransationOrder;
import fr.imcoding.edu365.enumeration.PaymentType;
import fr.imcoding.edu365.enumeration.TransactionType;
import fr.imcoding.edu365.persistence.entities.PaymeeInProgressTransaction;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;

@Service
@RequiredArgsConstructor
public class PaymeeFeesPaymentService {
	@Value("${edu365.payment.paymee.token}")
	private String token;

	@Value("${edu365.payment.paymee.vendor}")
	private Integer vendor;

	@Value("${edu365.payment.paymee.api.base.url}")
	private String requestPaymentUrl;

	@Value("${edu365.payment.paymee.api.request.check.payment.url}")
	private String requestCheckPaymentUrl;
	@Value("${edu365.payment.registration.fees}")
	private Integer registrationFee;

	private final UserFeesService userFeesService;
	private final PaymentService paymentService;

	private final PaymeeInprogressTransactionsService paymeeInprogressTransactionsService;

	public TransationOrder getRedirectionPaymentUrl() {
		JSONObject personJsonObject = new JSONObject();
		personJsonObject.put("amount", registrationFee);
		personJsonObject.put("vendor", vendor);
		personJsonObject.put("note", "Paiement des frais de création de compte Free Academy");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		headers.add("Authorization", "Token " + token);
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = new HttpEntity<String>(personJsonObject.toString(), headers);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.postForEntity(requestPaymentUrl + "create", request,
				String.class);
		PaymeeResponse result = new PaymeeResponse();
		try {
			result = new ObjectMapper().readValue(response.getBody(), PaymeeResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		TransationOrder transationOrder = new TransationOrder();
		transationOrder.setAmoount(result.getData().getAmount());
		transationOrder.setToken(result.getData().getToken());
		transationOrder.setRedirectUrl(requestCheckPaymentUrl.concat(result.getData().getToken()));
		paymeeInprogressTransactionsService.saveFeePaymeeTransaction(registrationFee, transationOrder.getToken());
		return transationOrder;
	}

	public PaymeeCheckPaymentResponse checkTransaction(String tokenRequest) {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		headers.add("Authorization", "Token " + token);
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = new HttpEntity<String>(headers);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.exchange(requestPaymentUrl.concat(tokenRequest).concat("/check"),
				HttpMethod.GET, request, String.class);
		PaymeeCheckPaymentResponse result = new PaymeeCheckPaymentResponse();
		try {
			result = new ObjectMapper().readValue(response.getBody(), PaymeeCheckPaymentResponse.class);
			System.out.println("resulttt::" + result.toString());

			if (result.getData() != null && result.getData().isPayment_status()) {
				PaymeeInProgressTransaction paymeeTransaction = paymeeInprogressTransactionsService
						.getByToken(tokenRequest);
				if (paymeeTransaction.getTransactionType() == TransactionType.USER_FEES) {
					userFeesService.savePaymentFeesOperation(paymeeTransaction.getUser(), PaymentType.PAYMEE);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return result;
	}

}
