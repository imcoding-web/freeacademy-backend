package fr.imcoding.edu365.rest;

import fr.imcoding.edu365.client.dtos.response.ExpertPaymentResponse;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.imcoding.edu365.business.services.PaymentService;
import fr.imcoding.edu365.business.services.payment.ExtPaymentService;
import fr.imcoding.edu365.client.dtos.response.PaymentResponse;
import fr.imcoding.edu365.dtos.MessageRequestDto;
import fr.imcoding.edu365.dtos.PaymentDto;
import fr.imcoding.edu365.dtos.ext.TransationOrder;
import fr.imcoding.edu365.enumeration.PaymentStatus;
import fr.imcoding.edu365.enumeration.PaymentType;
import fr.imcoding.edu365.utils.Constants;
import lombok.RequiredArgsConstructor;

/**
 * @author Rokaya
 * @Date 05/10/2022
 */

@RestController
@CrossOrigin
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {
	private final PaymentService paymentService;
	private final ExtPaymentService extPaymentservice;

	@PostMapping
	public ResponseEntity<String> payOffer(@ModelAttribute PaymentDto paymentDto) {
		String result = paymentService.payOffer(paymentDto);
		if(result.equals(Constants.OK))
			return ResponseEntity.ok(result);
		else
			return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
	}

	@GetMapping
	public List<PaymentResponse> getPaymentByExpert() {
		return paymentService.getOfferByUser();
	}

	@GetMapping("/get-redirect-url")
	public ResponseEntity<TransationOrder> getRedirectionPaymentUrl(@RequestParam("payment-type") PaymentType paymentType,
			@RequestParam("order-id") String orderId) {
		TransationOrder transactionOrder =  extPaymentservice.getRedirectionPaymentUrl(paymentType, orderId);
		if(transactionOrder == null)
			return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
		else
			return ResponseEntity.ok(transactionOrder);
	}

	@GetMapping("/payment-by-status")
	public List<PaymentResponse> getPaymentByStatus(@RequestParam("payment-status") PaymentStatus paymentStatus) {
		return paymentService.getPaymentByStatus(paymentStatus);
	}

	@GetMapping("/payment-by-uuid")
	public PaymentResponse getPaymentByUuid(@RequestParam("payment-uuid") UUID paymentUuid) {
		return paymentService.getPaymentByUuid(paymentUuid);
	}

	@GetMapping(value = "/validate")
	public void validate(@RequestParam("payment-uuid") UUID paymentUuid) {
		paymentService.validate(paymentUuid);
	}

	@PutMapping(value = "/refuse")
	public void refuse(@RequestParam("payment-uuid") UUID paymentUuid,
			@RequestBody MessageRequestDto messageRequestDto) {
		paymentService.refuse(paymentUuid, messageRequestDto);
	}

	@GetMapping(value = "/filter")
	public List<PaymentResponse> filterPayments(@RequestParam(value = "start-date", required = false) String startDate,
			@RequestParam(value = "end-date", required = false) String endDate,
			@RequestParam(value = "payment-type", required = false) String paymentType,
			@RequestParam(value = "expert-firstname", required = false) String expertFirstName,
			@RequestParam(value = "expert-lastname", required = false) String expertLastName


	) {
		return paymentService.getFilteredPayment(startDate, endDate, paymentType,expertFirstName,expertLastName);
	}

	@GetMapping(value = "/all-payments")
	public List<ExpertPaymentResponse> getAllPayments(@RequestParam(value = "payment-status") PaymentStatus paymentStatus) {
		return paymentService.getAllPayments(paymentStatus);
	}

	}
