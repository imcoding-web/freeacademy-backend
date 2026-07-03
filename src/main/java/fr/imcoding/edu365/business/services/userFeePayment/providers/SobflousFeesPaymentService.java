package fr.imcoding.edu365.business.services.userFeePayment.providers;

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
import fr.imcoding.edu365.business.services.SobflousInprogressTransactionsService;
import fr.imcoding.edu365.business.services.UserService;
import fr.imcoding.edu365.dtos.ext.TransationOrder;
import fr.imcoding.edu365.persistence.entities.SobflousInprogressTransaction;
import fr.imcoding.edu365.persistence.entities.User;
import fr.imcoding.edu365.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class SobflousFeesPaymentService {

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

	@Value("${edu365.payment.registration.fees}")
	private Integer registrationFee;

	private final UserService userService;

	private final SobflousInprogressTransactionsService sobflousInprogressTransactionsService;

	public TransationOrder getRedirectionPaymentUrl() {
		User user = userService.getCurrentUser();
		// Offer offer = offerService.getOfferById(orderId);
		SobflousInprogressTransaction inprogressTransactions = sobflousInprogressTransactionsService
				.saveSobflousFeeTransaction(user);

		MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
		form.add("SHOP_ID", shopId);
		form.add("ID_CLIENT_M", user.getId().toString());
		form.add("TRANSM_ID", inprogressTransactions.getTransmid());
		form.add("AMOUNT", Utils.convertAmountToStringWithSeperator(registrationFee));
		String tokenRequest = Utils.getMd5(shopPwd) + Utils.getMd5(shopId)
				+ Utils.getMd5(inprogressTransactions.getTransmid())
				+ Utils.getMd5(Utils.convertAmountToStringWithSeperator(registrationFee));
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
		ResponseEntity<String> response = restTemplate.postForEntity(requestPaymentUrl.concat("demandepaiement"),
				requestEntity, String.class);
		SobflousTokenResultResponse result = new SobflousTokenResultResponse();
		try {
			result = new ObjectMapper().readValue(response.getBody(), SobflousTokenResultResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		TransationOrder transationOrder = new TransationOrder();
		transationOrder.setOrderId(result.getResult().TRANSM_ID);
		transationOrder.setRedirectUrl(result.getResult().URL);
		System.out.println("transationOrder::" + transationOrder.toString());
		// transationOrder.setAmoount(result.getResult().getAMOUNT());
		return transationOrder;
	}
}