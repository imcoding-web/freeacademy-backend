package fr.imcoding.edu365.business.services.payment.providers;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.imcoding.edu365.business.ext.sobflous.response.SobflousTokenResultResponse;
import fr.imcoding.edu365.business.ext.sobflous.response.SobflousTransactionStatusResultResponse;
import fr.imcoding.edu365.business.ext.sobflous.response.SobflousTransactionVerificationResultResponse;
import fr.imcoding.edu365.business.services.OfferService;
import fr.imcoding.edu365.business.services.PaymentService;
import fr.imcoding.edu365.business.services.SobflousInprogressTransactionsService;
import fr.imcoding.edu365.business.services.UserFeesService;
import fr.imcoding.edu365.business.services.UserService;
import fr.imcoding.edu365.dtos.ext.TransationOrder;
import fr.imcoding.edu365.enumeration.PaymentType;
import fr.imcoding.edu365.enumeration.TransactionType;
import fr.imcoding.edu365.persistence.entities.Offer;
import fr.imcoding.edu365.persistence.entities.SobflousInprogressTransaction;
import fr.imcoding.edu365.persistence.entities.User;
import fr.imcoding.edu365.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class SobflousPaymentService {

	@Value("${edu365.payment.sobflous.shopId}")
	private String shopId;

	@Value("${edu365.payment.sobflous.password}")
	private String shopPwd;

	@Value("${edu365.payment.sobflous.shopCode}")
	private String shopCode;

	@Value("${edu365.payment.sobflous.bonus}")
	private Integer discount;

	@Value("${edu365.payment.sobflous.api.base.url}")
	private String requestPaymentUrl;

	/*@Value("${edu365.payment.sobflous.api.request.transaction.verification.url}")
	private String transactionverificationUrl;*/

	private final UserService userService;
	private final OfferService offerService;
	private final PaymentService paymentService;
	private final UserFeesService userFeesService;

	private final SobflousInprogressTransactionsService sobflousInprogressTransactionsService;

	public TransationOrder getRedirectionPaymentUrl(String orderId) {
		User user = userService.getCurrentUser();
		Offer offer = offerService.getOfferById(orderId);
		SobflousInprogressTransaction inprogressTransactions = sobflousInprogressTransactionsService
				.saveSobflousTransaction(offer);

		MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
		form.add("SHOP_ID", shopId);
		form.add("ID_CLIENT_M", user.getId().toString());
		form.add("TRANSM_ID", inprogressTransactions.getTransmid());
		form.add("AMOUNT", Utils.convertAmountToStringWithSeperator(offer.getOfferPriceToPay()));
		String tokenRequest = Utils.getMd5(shopPwd) + Utils.getMd5(shopId)
				+ Utils.getMd5(inprogressTransactions.getTransmid())
				+ Utils.getMd5(Utils.convertAmountToStringWithSeperator(offer.getOfferPriceToPay()));
		tokenRequest = Utils.encryptSHA512(tokenRequest);
		form.add("TOKEN", tokenRequest);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(form, headers);
		CloseableHttpClient httpClient = HttpClients.custom().setSSLHostnameVerifier(new NoopHostnameVerifier())
				.build();
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setHttpClient(httpClient);
		RestTemplate restTemplate = new RestTemplate(requestFactory);
		ResponseEntity<String> response = restTemplate.postForEntity(requestPaymentUrl.concat("demandepaiement"), requestEntity, String.class);
		SobflousTokenResultResponse result = new SobflousTokenResultResponse();
		try {
			result = new ObjectMapper().readValue(response.getBody(), SobflousTokenResultResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		TransationOrder transationOrder=new TransationOrder();
		transationOrder.setOrderId(result.getResult().TRANSM_ID);
		transationOrder.setRedirectUrl(result.getResult().URL);
		//transationOrder.setAmoount(result.getResult().getAMOUNT());
		return transationOrder;
	}

	public SobflousTransactionVerificationResultResponse trackSobflousTransaction(String transmId) {
		MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
		form.add("SHOP_ID", shopId);
		form.add("TRANSM_ID", transmId);
		String tokenRequest = Utils.getMd5(shopPwd) + Utils.getMd5(shopId) + Utils.getMd5(transmId);
		tokenRequest = Utils.encryptSHA512(tokenRequest);
		form.add("TOKEN", tokenRequest);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(form, headers);
		CloseableHttpClient httpClient = HttpClients.custom().setSSLHostnameVerifier(new NoopHostnameVerifier())
				.build();
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setHttpClient(httpClient);
		RestTemplate restTemplate = new RestTemplate(requestFactory);
		ResponseEntity<String> response = restTemplate.postForEntity(requestPaymentUrl.concat("verificationtransaction"), requestEntity,
				String.class);
		System.out.println("Le résultat de vérification de transaction " + response);

		SobflousTransactionVerificationResultResponse result = new SobflousTransactionVerificationResultResponse();
		try {
			result = new ObjectMapper().readValue(response.getBody(), SobflousTransactionVerificationResultResponse.class);
			if(result.getResult().getTRANSACTION_STATE().equals("CONFIRMED")){
				//if(trackSobflousTransactionStatus(result.getResult().getTRANSS_ID()).getResult().getTRANS_STATE().equals("success")){
				SobflousInprogressTransaction inprogressTransaction = sobflousInprogressTransactionsService.getByTransmId(transmId);
				if(inprogressTransaction.getTransactionType() == TransactionType.OFFER) {
					Offer offer=sobflousInprogressTransactionsService.getByTransmId(transmId).getOffer();
					paymentService.savePaymentOperation(offer,PaymentType.SOBFLOUS);
				} else if(inprogressTransaction.getTransactionType() == TransactionType.USER_FEES) {
					User user =sobflousInprogressTransactionsService.getByTransmId(transmId).getUser();
					userFeesService.savePaymentFeesOperation(user, PaymentType.SOBFLOUS);
				}
				
					
				//}

			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public SobflousTransactionStatusResultResponse trackSobflousTransactionStatus(String transId) {
		MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
		form.add("SHOP_ID", shopId);
		form.add("SOBFLOUS_TRANS_ID", transId);
		String tokenRequest = Utils.getMd5(shopPwd) + Utils.getMd5(shopId) + Utils.getMd5(transId);
		tokenRequest = Utils.encryptSHA512(tokenRequest);
		form.add("TOKEN", tokenRequest);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(form, headers);
		CloseableHttpClient httpClient = HttpClients.custom()
				.setSSLHostnameVerifier(new NoopHostnameVerifier())
				.build();
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setHttpClient(httpClient);
		RestTemplate restTemplate = new RestTemplate(requestFactory);
		ResponseEntity<String> response = restTemplate
				.postForEntity(requestPaymentUrl.concat("statustransaction"), requestEntity,
						String.class);
		System.out.println("Transaction status result " + response);

		SobflousTransactionStatusResultResponse result = new SobflousTransactionStatusResultResponse();
		try {
			result = new ObjectMapper().readValue(response.getBody(), SobflousTransactionStatusResultResponse.class);

			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}