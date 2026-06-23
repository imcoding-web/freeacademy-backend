package fr.imcoding.edu365.rest.mock;

import fr.imcoding.edu365.business.ext.paymee.response.PaymeeCheckPaymentResponse;
import fr.imcoding.edu365.business.services.payment.providers.PaymeePaymentService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Rokaya
 * @Date 12/12/2022
 */
@RestController
@CrossOrigin
@RequestMapping("/paymee-payment")
@RequiredArgsConstructor
public class PaymeeController {
  private final PaymeePaymentService paymeePaymentService;

  @GetMapping
  public PaymeeCheckPaymentResponse checkTransaction(@RequestParam("token") String token){
    return paymeePaymentService.checkTransaction(token);
  }

}
