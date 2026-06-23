package fr.imcoding.edu365.rest.mock;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.imcoding.edu365.dtos.ext.PaymentRequest;
import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin
@RequestMapping("/ext-payment")
@RequiredArgsConstructor
public class ExtPaymentController {

	@PostMapping()
	public ResponseEntity<Void> pay(@RequestBody PaymentRequest paymentRequest) {
		return ResponseEntity.ok().build();
	}



}
