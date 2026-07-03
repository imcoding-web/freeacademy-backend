package fr.imcoding.edu365.business.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import fr.imcoding.edu365.business.mappers.PaymentRequestMapper;
import fr.imcoding.edu365.client.dtos.request.PaymentRequestDto;
import fr.imcoding.edu365.client.dtos.response.PaymentRequestResponse;
import fr.imcoding.edu365.enumeration.PaymentRequestStatus;
import fr.imcoding.edu365.persistence.entities.InformationGiver;
import fr.imcoding.edu365.persistence.entities.PaymentRequest;
import fr.imcoding.edu365.persistence.entities.UserBankData;
import fr.imcoding.edu365.persistence.repositories.PaymentRequestRepository;
import lombok.RequiredArgsConstructor;

/**
 * @author Rokaya
 * @Date 25/12/2022
 */
@Service
@RequiredArgsConstructor
public class PaymentRequestService {

  private final PaymentRequestRepository paymentRequestRepository;
  private final PaymentRequestMapper paymentRequestMapper;
  private final UserService userService;
  private final UserBankDataService userBankDataService;

  public PaymentRequest savePaymentRequest(PaymentRequestDto PaymentRequestRequest){
    InformationGiver expert=(InformationGiver)userService.getCurrentUser();
    PaymentRequest paymentRequest =new PaymentRequest();
    paymentRequest.setAmount(PaymentRequestRequest.getAmount());
    paymentRequest.setExpert(expert);
    paymentRequest.setPaymentRequestStatus(PaymentRequestStatus.PENDING);
    return paymentRequestRepository.save(paymentRequest);
  }

  public PaymentRequest validatePaymentRequest(PaymentRequestDto PaymentRequestRequest){
    PaymentRequest paymentRequest = paymentRequestRepository.findByUuid(PaymentRequestRequest.getUuid());
    paymentRequest.setAmount(PaymentRequestRequest.getAmount());
    paymentRequest.setPaymentRequestStatus(PaymentRequestStatus.VALIDATED);
    UserBankData userBankData=userBankDataService.getUserBankData(paymentRequest.getExpert().getUuid());
    userBankData.setUnpaidAccumulatedBalance(userBankData.getUnpaidAccumulatedBalance()-PaymentRequestRequest.getAmount());
    userBankData.setAccumulatedBalance(userBankData.getAccumulatedBalance()+PaymentRequestRequest.getAmount());

    userBankDataService.saveUserBankData(userBankData);
    return paymentRequestRepository.save(paymentRequest);
  }

  public List<PaymentRequestDto> getByExpertUuid(UUID uuid){
    return paymentRequestRepository.findByExpertUuid(uuid).stream().map(paymentRequestMapper::toPaymentRequestDto).collect(Collectors.toList());

  }

  public List<PaymentRequestResponse> getPaymentRequest(PaymentRequestStatus paymentRequestStatus){
    return paymentRequestRepository.findByPaymentRequestStatus(paymentRequestStatus).stream().map(
        paymentRequestMapper::toPaymentRequestResponse).collect(Collectors.toList());

  }

  public PaymentRequestResponse getPaymentRequestDetails(UUID transferUuid){
    PaymentRequest paymentRequest = paymentRequestRepository.findByUuid(transferUuid);
     UserBankData userBankData=userBankDataService.getUserBankData(paymentRequest.getExpert().getUuid());
     return paymentRequestMapper.toPaymentRequestValidationResponse(paymentRequest,userBankData);

  }

  public boolean hasPendingPaymentRequest(){
    InformationGiver currentUser=(InformationGiver)userService.getCurrentUser();
    return paymentRequestRepository.findByExpertUuid(currentUser.getUuid()).stream()
        .filter(field->field.getPaymentRequestStatus()
            .equals(PaymentRequestStatus.PENDING))
        .findFirst().isPresent();
  }


}
