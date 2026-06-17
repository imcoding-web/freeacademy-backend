package fr.imcoding.edu365.rest;

import fr.imcoding.edu365.business.services.UserFeesService;
import fr.imcoding.edu365.business.services.userFeePayment.ExternPaymentService;
import fr.imcoding.edu365.client.dtos.response.PaymentFeesResponse;
import fr.imcoding.edu365.client.dtos.response.UserFeesPaymenrResponse;
import fr.imcoding.edu365.dtos.MessageRequestDto;
import fr.imcoding.edu365.dtos.UserFeesDto;
import fr.imcoding.edu365.dtos.ext.TransationOrder;
import fr.imcoding.edu365.enumeration.FeesType;
import fr.imcoding.edu365.enumeration.PaymentStatus;
import fr.imcoding.edu365.enumeration.PaymentType;
import fr.imcoding.edu365.utils.Constants;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
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

/**
 * @author Rokaya
 * @Date 27/01/2023
 */
@RestController
@CrossOrigin
@RequestMapping("/user-fees")
@RequiredArgsConstructor
public class UserFeesController {
  private final UserFeesService userFeesService;
  private final ExternPaymentService externPaymentservice;

  @PostMapping
  public ResponseEntity<String> payOffer(@ModelAttribute UserFeesDto paymentDto) {
    String result = userFeesService.payFees(paymentDto);
    if(result.equals(Constants.OK))
      return ResponseEntity.ok(result);
    else
      return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
  }
  
  @PostMapping("/pay-with-promotion-code")
  public ResponseEntity<String> payWithPromotionCode(@ModelAttribute UserFeesDto paymentDto) {
    String result = userFeesService.payWithPromotionCode(paymentDto);
    if(result.equals(Constants.OK))
      return ResponseEntity.ok(result);
    else if (result.equals(Constants.CONFLICT))
    	return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
    else
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
  }

  @GetMapping
  public UserFeesPaymenrResponse getFeePaymentByExpert(@RequestParam(value = "fee-type") FeesType feesType) {
    return userFeesService.getFeePaymentByExpert(feesType);
  }

  @GetMapping("/get-redirect-url")
  public ResponseEntity<TransationOrder> getRedirectionPaymentUrl(@RequestParam("payment-type") PaymentType paymentType) {
    TransationOrder transactionOrder =  externPaymentservice.getRedirectionPaymentUrl(paymentType);
    if(transactionOrder == null)
      return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
    else
      return ResponseEntity.ok(transactionOrder);
  }


  @GetMapping("/payment-fee-by-status")
  public List<PaymentFeesResponse> getPaymentByStatus(@RequestParam("payment-status") PaymentStatus paymentStatus) {
    return userFeesService.getUserFeesByPaymentStatus(paymentStatus);
  }

  @GetMapping("/payment-by-uuid")
  public PaymentFeesResponse getPaymentFeeByUuid(@RequestParam("payment-uuid") UUID paymentUuid) {
    return userFeesService.getUserFeesByUuid(paymentUuid);
  }

  @GetMapping(value = "/validate")
  public void validate(@RequestParam("payment-uuid") UUID paymentUuid) {
    userFeesService.validate(paymentUuid);
  }

  @PutMapping(value = "/refuse")
  public void refuse(@RequestParam("payment-uuid") UUID paymentUuid,
      @RequestBody MessageRequestDto messageRequestDto) {
    userFeesService.refuse(paymentUuid, messageRequestDto);
  }
}
