package fr.imcoding.edu365.business.ext.sobflous.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import fr.imcoding.edu365.business.ext.sobflous.request.SobflousTokenRequest;
import fr.imcoding.edu365.business.ext.sobflous.response.SobflousTokenResponse;

@FeignClient(name = "sobflous", url = "${edu365.payment.sobflous.api.base.url}")
public interface SobflousFeignClient {

	@PostMapping(value="demandepaiement")
	ResponseEntity<SobflousTokenResponse> getToken(@ModelAttribute SobflousTokenRequest sobflousTokenRequest);

}