package fr.imcoding.edu365.rest;

import fr.imcoding.edu365.business.services.PaymentRequestService;
import fr.imcoding.edu365.client.dtos.request.PaymentRequestDto;
import fr.imcoding.edu365.client.dtos.response.PaymentRequestResponse;
import fr.imcoding.edu365.enumeration.PaymentRequestStatus;
import fr.imcoding.edu365.persistence.entities.PaymentRequest;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Rokaya
 * @Date 26/12/2022
 */
@RestController
@CrossOrigin
@RequestMapping("/payment-request")
@RequiredArgsConstructor
public class PaymentRequestController {

  private final PaymentRequestService paymentRequestService;

  @PreAuthorize("hasAnyAuthority({'ADMINISTRATOR'})")
  @PostMapping
  public PaymentRequest validateExpertPaymentRequest(@RequestBody PaymentRequestDto paymentRequestDto){
    return paymentRequestService.validatePaymentRequest(paymentRequestDto);
  }

  @PreAuthorize("hasAnyAuthority({'INFORMATION_GIVER'})")
  @PostMapping(value = "add-transfer-request")
  public PaymentRequest saveExpertPaymentRequest(@RequestBody PaymentRequestDto paymentRequestDto){
    return paymentRequestService.savePaymentRequest(paymentRequestDto);
  }

  @PreAuthorize("hasAnyAuthority({'ADMINISTRATOR'})")
  @GetMapping
  public List<PaymentRequestResponse> getTransfertRequest(@RequestParam(value = "transction-status") PaymentRequestStatus paymentRequestStatus){
    return paymentRequestService.getPaymentRequest(paymentRequestStatus);
  }

  @PreAuthorize("hasAnyAuthority({'ADMINISTRATOR'})")
  @GetMapping("payment-request-details")
  public PaymentRequestResponse getTransfertRequestDetails(@RequestParam(value = "request-uuid") UUID transferUuid){
    return paymentRequestService.getPaymentRequestDetails(transferUuid);
  }
  @GetMapping(value = "has-pending-request-payment")
  public boolean hasPendingRequestPayment() {
    return paymentRequestService.hasPendingPaymentRequest();
  }
}
