package fr.imcoding.edu365.business.services.payment.providers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.imcoding.edu365.business.ext.paymee.request.PaymeeTokenRequest;
import fr.imcoding.edu365.business.ext.paymee.response.PaymeeCheckPaymentResponse;
import fr.imcoding.edu365.business.ext.paymee.response.PaymeeResponse;
import fr.imcoding.edu365.business.services.OfferService;
import fr.imcoding.edu365.business.services.PackageSubscriptionService;
import fr.imcoding.edu365.business.services.PaymeeInprogressTransactionsService;
import fr.imcoding.edu365.business.services.PaymentService;
import fr.imcoding.edu365.client.dtos.request.PackageSubscriptionRequest;
import fr.imcoding.edu365.dtos.ext.TransationOrder;
import fr.imcoding.edu365.enumeration.PaymentType;
import fr.imcoding.edu365.persistence.entities.Offer;
import fr.imcoding.edu365.persistence.entities.PaymeeInProgressTransaction;
import fr.imcoding.edu365.persistence.entities.SkillAreaPackage;
import fr.imcoding.edu365.persistence.repositories.PaymeeInprogressTransactionsRepository;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
@RequiredArgsConstructor
public class PaymeePaymentService {
  @Value("${edu365.payment.paymee.token}")
  private String token;

  @Value("${edu365.payment.paymee.vendor}")
  private Integer vendor;

  @Value("${edu365.payment.paymee.api.base.url}")
  private String requestPaymentUrl;

  @Value("${edu365.payment.paymee.api.request.check.payment.url}")
  private String requestCheckPaymentUrl;

private final OfferService offerService;
  private final PaymentService paymentService;
  private final PackageSubscriptionService packageSubscriptionService;

private final PaymeeInprogressTransactionsService paymeeInprogressTransactionsService;



  public TransationOrder getRedirectionPaymentUrl(double amount, String orderIdentifier) {
    //Offer offer = offerService.getOfferById(orderId);
     JSONObject personJsonObject = new JSONObject();
    //personJsonObject.put("amount", offer.getOfferPriceToPay());
      personJsonObject.put("amount", amount);
    personJsonObject.put("vendor", vendor);
    //personJsonObject.put("note", "Order #"+offer.getUniqueIdentifier());
    personJsonObject.put("note", "Order #"+orderIdentifier);

    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json");
    headers.add("Authorization", "Token "+token);
   headers.setContentType(MediaType.APPLICATION_JSON);
   HttpEntity<String> request =
        new HttpEntity<String>(personJsonObject.toString(), headers);
    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<String> response = restTemplate.postForEntity(requestPaymentUrl+"create", request, String.class);
    PaymeeResponse result = new PaymeeResponse();
    try {
      result = new ObjectMapper().readValue(response.getBody(),PaymeeResponse.class);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
    return saveNewTransactionOrder(result.getData().getAmount(),result.getData().getToken());
  }

  private TransationOrder saveNewTransactionOrder(double amount, String token) {
      TransationOrder transationOrder=new TransationOrder();
      transationOrder.setAmoount(amount);
      transationOrder.setToken(token);
      transationOrder.setRedirectUrl(requestCheckPaymentUrl.concat(token));

      return transationOrder;
  }
   public  PaymeeInProgressTransaction savePaymeeTransaction(Offer offer, SkillAreaPackage pack, String token) {
      return  paymeeInprogressTransactionsService.savePaymeeTransaction(offer, pack , token);
    }



  public PaymeeCheckPaymentResponse checkTransaction(String tokenRequest) {

    JSONObject personJsonObject = new JSONObject();
    personJsonObject.put("token", tokenRequest);
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json");
    headers.add("Authorization", "Token "+token);
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> request =
        new HttpEntity<String>(headers);
    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<String> response = restTemplate.exchange(requestPaymentUrl.concat(token).concat("/check"), HttpMethod.GET,request, String.class);
    PaymeeCheckPaymentResponse result = new PaymeeCheckPaymentResponse();
    try {
      result = new ObjectMapper().readValue(response.getBody(),PaymeeCheckPaymentResponse.class);
      if(result.getData()!=null && result.getData().isPayment_status()){
          PaymeeInProgressTransaction paymeeTransaction = paymeeInprogressTransactionsService.getByToken(result.getData().getToken());
          // On va traiter uniquement le paiement des abonnements
          // A RAFFINER POUR SUPPORTER LE PAIEMENT POUR UN SEUL MATIERE: ACTUELLEMENT LE MATIERE EST FORCE A NULL POU DIRE QUE LE PAIEMENT EST POUR TOUT LE PACK
            if (paymeeTransaction != null && paymeeTransaction.getPack() != null) {
                PackageSubscriptionRequest subscription = new PackageSubscriptionRequest(paymeeTransaction.getUser().getUuid(), 1, paymeeTransaction.getPack().getPackageType(), null);
                packageSubscriptionService.addPackageSubscription(subscription);
            }
        //Offer offer=paymeeInprogressTransactionsService.getByToken(result.getData().getToken()).getOffer();
        //paymentService.savePaymentOperation(offer,PaymentType.PAYMEE);
      }
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }

    return result;
  }


}
