package fr.imcoding.edu365.business.ext.runpay.rest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.imcoding.edu365.business.ext.runpay.response.RunpayOrderInfoResponse;
import fr.imcoding.edu365.business.ext.runpay.response.RunpaySetOrderStatusResponse;
import fr.imcoding.edu365.business.services.payment.providers.RunpayPaymentService;
import lombok.RequiredArgsConstructor;

/**
 * @author Rokaya
 * @Date 22/11/2022
 */

@RestController
@CrossOrigin
@RequestMapping("/runpay")
@RequiredArgsConstructor
public class RunpayController {

	private final RunpayPaymentService runpaypaymentService;

	@GetMapping("/get-order-info")
	public RunpayOrderInfoResponse getOrderInfo(@RequestParam(value = "ID") String orderId,
			@RequestParam(value = "task", required = false, defaultValue = "getOrderInfos") String task,
			@RequestParam(value = "format", required = false, defaultValue = "json") String format,
			@RequestParam(value = "token", required = false) String token, @RequestParam(value = "ID") String id) {
		System.out.println("L'endpoint get-order-info de Runpay a été appellé avec les paramétres orderId= " + orderId
				+ " task= " + task + " format= " + format + " token " + token + " ID " + id);
		return runpaypaymentService.getRunpayOrderInfo(orderId);
	}

	@GetMapping("/set-order-status")
	public RunpaySetOrderStatusResponse setOrderStatus(@RequestParam(value = "ID") String orderId,
			@RequestParam(value = "task", required = false, defaultValue = "setOrderStatus") String task,
			@RequestParam(value = "format", required = false, defaultValue = "json") String format,
			@RequestParam(value = "amount") String amount, @RequestParam(value = "key", required = false) String key,
			@RequestParam(value = "token", required = false) String token) {
		System.out.println("L'endpoint set-order-status de Runpay a été appellé avec les paramétres orderId= " + orderId
				+ " task= " + task + " format= " + format + " token " + token + " ID " + orderId + " amount " + amount);
		return runpaypaymentService.setRunpayOrderStatus(orderId, amount);
	}

	@PostMapping("/notification")
	public void trackNotificationPayment(@RequestBody String body) {
		System.out.println("run pay notification a été appellée avec le paamétre " + body);
	}

	@PostMapping("/ok")
	public void trackOkPayment(@RequestBody String body) {
		System.out.println("run pay OK a été appellée avec le paamétre " + body);
	}

	@PostMapping("/ko")
	public void trackKOPayment(@RequestBody String body) {
		System.out.println("run pay KO a été appellée avec le paamétre " + body);
	}

}
