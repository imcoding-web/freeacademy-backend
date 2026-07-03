package fr.imcoding.edu365.business.ext.sobflous.rest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.imcoding.edu365.business.services.payment.providers.SobflousPaymentService;
import lombok.RequiredArgsConstructor;

/**
 * @author Rokaya
 * @Date 05/10/2022
 */

@RestController
@CrossOrigin
@RequestMapping("/sobflous")
@RequiredArgsConstructor
public class SobflousController {
	private final SobflousPaymentService sobflousPaymentService;

	@GetMapping(value = "ok")
	public void trackOKSobflousTransaction(@RequestParam(value = "transm_id", required = false, defaultValue = "0") String transmId,
			@RequestParam(value = "state", required = false, defaultValue = "0") String state) {
		System.out.println("Le WS de sobflous OK a été appéllé avec les deux paramétres  TRANSM_ID= " + transmId
				+ " et State= " + state);
		if(transmId != null && state.equals("s")) {
			sobflousPaymentService.trackSobflousTransaction(transmId);
		}
	}
	
	@GetMapping(value = "ko")
	public void trackKOSobflousTransaction(@RequestParam(value = "transm_id", required = false) String transmId,
			@RequestParam(value = "state", required = false) String state) {
		System.out.println("Le WS de sobflous OK a été appéllé avec les deux paramétres  TRANSM_ID= " + transmId
				+ " et State= " + state);
	}

}
