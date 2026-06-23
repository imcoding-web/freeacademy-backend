package fr.imcoding.edu365.rest.mock;

import fr.imcoding.edu365.business.ext.paymee.response.PaymeeCheckPaymentResponse;
import fr.imcoding.edu365.business.services.userFeePayment.providers.PaymeeFeesPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Rokaya
 * @Date 29/01/2023
 */
@RestController
@CrossOrigin
@RequestMapping("/fee-paymee-payment")
@RequiredArgsConstructor
public class FeePaymeeController {
  private final PaymeeFeesPaymentService paymeePaymentService;

  @GetMapping
  public PaymeeCheckPaymentResponse checkTransaction(@RequestParam("token") String token){
    return paymeePaymentService.checkTransaction(token);
  }

}
