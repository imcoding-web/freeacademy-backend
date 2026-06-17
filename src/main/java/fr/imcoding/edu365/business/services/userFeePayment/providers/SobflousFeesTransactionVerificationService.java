package fr.imcoding.edu365.business.services.userFeePayment.providers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.imcoding.edu365.business.ext.sobflous.response.SobflousTokenResultResponse;
import fr.imcoding.edu365.business.ext.sobflous.response.SobflousTransactionVerificationResponse;
import fr.imcoding.edu365.business.services.OfferService;
import fr.imcoding.edu365.business.services.SobflousInprogressTransactionsService;
import fr.imcoding.edu365.business.services.UserService;
import fr.imcoding.edu365.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class SobflousFeesTransactionVerificationService {

	@Value("${edu365.payment.sobflous.shopId}")
	private String shopId;

	@Value("${edu365.payment.sobflous.password}")
	private String shopPwd;

	@Value("${edu365.payment.sobflous.shopCode}")
	private String shopCode;

	@Value("${edu365.payment.sobflous.bonus}")
	private Integer discount;
	
	@Value("${edu365.payment.sobflous.api.request.payment.url}")
	private String requestPaymentUrl;
	
	@Value("${edu365.payment.sobflous.api.request.transaction.verification.url}")
	private String transactionverificationUrl;

	private final UserService userService;
	private final OfferService offerService;
	private final SobflousInprogressTransactionsService sobflousInprogressTransactionsService;


	@Async
	public void trackSobflousTransaction(String transmId, SobflousTokenResultResponse paymentRequestResponse) {
		MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
		form.add("SHOP_ID", shopId);
		form.add("TRANSM_ID", transmId);
		String tokenRequest =
				Utils.getMd5(shopPwd) + Utils.getMd5(shopId) + Utils.getMd5(transmId);
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
		ResponseEntity<String> response = restTemplate.postForEntity(
				transactionverificationUrl, requestEntity,
				String.class);
		SobflousTransactionVerificationResponse result = new SobflousTransactionVerificationResponse();
		try {
			result = new ObjectMapper().readValue(response.getBody(), SobflousTransactionVerificationResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
