package fr.imcoding.edu365.business.services;

import fr.imcoding.edu365.business.mappers.UserFeesPaymentMapper;
import fr.imcoding.edu365.business.services.email.EmailService;
import fr.imcoding.edu365.client.dtos.response.PaymentFeesResponse;
import fr.imcoding.edu365.client.dtos.response.UserFeesPaymenrResponse;
import fr.imcoding.edu365.dtos.EmailDto;
import fr.imcoding.edu365.dtos.MessageRequestDto;
import fr.imcoding.edu365.dtos.UserFeesDto;
import fr.imcoding.edu365.enumeration.EmailContext;
import fr.imcoding.edu365.enumeration.FeesType;
import fr.imcoding.edu365.enumeration.MediaContext;
import fr.imcoding.edu365.enumeration.PaymentStatus;
import fr.imcoding.edu365.enumeration.PaymentType;
import fr.imcoding.edu365.enumeration.TransactionType;
import fr.imcoding.edu365.persistence.entities.InformationGiver;
import fr.imcoding.edu365.persistence.entities.Media;
import fr.imcoding.edu365.persistence.entities.User;
import fr.imcoding.edu365.persistence.entities.UserFees;
import fr.imcoding.edu365.persistence.repositories.UserFeesRepository;
import fr.imcoding.edu365.utils.Constants;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Rokaya
 * @Date 27/01/2023
 */
@Service
@RequiredArgsConstructor
public class UserFeesService {

  private final MediaService mediaService;
  private final UserService userService;
  private final UserFeesRepository userFeesRepository;
  private final UserFeesPaymentMapper userFeesPaymentMapper;
  private final EmailService emailService;
  private final PromotionService promotionService;


  @Value("${edu365.payment.registration.fees}")
  private Integer registrationFee;
  @Transactional
  public String payFees(UserFeesDto paymentDto) {
    InformationGiver user = (InformationGiver) userService.getCurrentUser();
    if (!userFeesRepository.findByUserUuid(user.getUuid()).isPresent()) {

      UserFees userFees = new UserFees();
      userFees.setPaymentStatus(PaymentStatus.PENDING_VALIDATION);
      userFees.setPaymentType(paymentDto.getPaymentType());
      userFees.setUser(user);
      userFees.setAmountPaid(registrationFee);
      userFees.setDatePaiement(new Date());
      userFees.setFeesType(FeesType.REGISTRATION);

      if (paymentDto.getPaymentMedia() != null) {
        try {
          Media media = mediaService
              .saveMedia(paymentDto.getPaymentMedia(), MediaContext.PAYMENT_DOCUMENT);
          userFees.setPaymentMedia(media);
        } catch (Exception e) {
          e.printStackTrace();
        }

      }

      userFeesRepository.save(userFees);
      return Constants.OK;
    }
    return Constants.CONFLICT;

  }

  public UserFees getUserFeesById(String orderId){
    return userFeesRepository.findByUniqueIdentifier(orderId);
  }
  public UserFees getUserFeesByUserUuid(UUID userUuid,FeesType feesType){
    return userFeesRepository.findByUserUuidAndFeesType(userUuid,feesType);
  }
  public UserFees getUserFeesByUserUuidFeesTypePaymentStatus(UUID userUuid,FeesType feesType,PaymentStatus paymentStatus){
    return userFeesRepository.findByUserUuidAndFeesTypeAndPaymentStatus(userUuid,feesType,paymentStatus);
  }
  public UserFees getUserFeesByUserUuidFeesType(UUID userUuid,FeesType feesType){
    return userFeesRepository.findByUserUuidAndFeesType(userUuid,feesType);
  }

  public UserFeesPaymenrResponse getFeePaymentByExpert( FeesType feesType) {
    InformationGiver user=(InformationGiver)userService.getCurrentUser();
    return userFeesPaymentMapper.toPaymentResponse(userFeesRepository.findByUserUuidAndFeesType(user.getUuid(),feesType),user.isExemptFromFees());
  }

  public String savePaymentFeesOperation(User user, PaymentType paymentType) {
    InformationGiver informationGiver = (InformationGiver) user;
    if (!userFeesRepository.findByUserUuid(user.getUuid()).isPresent()) {

      UserFees userFees = new UserFees();
      userFees.setPaymentStatus(PaymentStatus.VALIDATED);
      userFees.setPaymentType(paymentType);
      userFees.setUser(informationGiver);
      userFees.setAmountPaid(registrationFee);
      userFees.setDatePaiement(new Date());
      userFees.setFeesType(FeesType.REGISTRATION);
      userFeesRepository.save(userFees);
      return Constants.OK;
    }
    return Constants.CONFLICT;

  }
  public List<PaymentFeesResponse> getUserFeesByPaymentStatus(PaymentStatus paymentStatus){
    return userFeesRepository.findByPaymentStatus(paymentStatus).stream().map(userFeesPaymentMapper::toPaymentFeeResponse).collect(Collectors.toList());
  }
  public PaymentFeesResponse getUserFeesByUuid(UUID paymentUuid){
    return userFeesPaymentMapper.toPaymentFeeResponse(userFeesRepository.findByUuid(paymentUuid).orElse(null));
  }


  public void validate(UUID paymentUuid) {
    UserFees payment = userFeesRepository.findByUuid(paymentUuid).orElse(null);
    payment.setPaymentStatus(PaymentStatus.VALIDATED);
    userFeesRepository.save(payment);


    // send email to expert
    List<String> destination = Arrays.asList(payment.getUser().getUserEmail());
    Map<String, Object> maps = new HashMap<>();

    EmailDto emailDto =
        new EmailDto(
            Constants.MAIL_SUBJECT_EXPERT_PAYMENT_FEE_VALID, "notif-expert-paiement-fees-valid.html", maps,
            new HashMap<>(),EmailContext.NOTIF_EXPERT_PAYMENT_FEES_VALID);

    emailService.sendMail(emailDto, destination);

  }

  public void refuse(UUID uuid,MessageRequestDto messageRequest) {
    UserFees payment = userFeesRepository.findByUuid(uuid).orElse(null);
    payment.setPaymentStatus(PaymentStatus.REFUSED);
    payment.setRefusalReason(messageRequest.getMessage());
    userFeesRepository.save(payment);
    // send email
   List<String> destination = Arrays.asList(payment.getUser().getUserEmail());
    Map<String, Object> maps = new HashMap<>();
    maps.put("refusalReason", messageRequest.getMessage());
    EmailDto emailDto =
        new EmailDto(
            Constants.MAIL_SUBJECT_EXPERT_PAYMENT_FEE_NOT_VALID, "notif-expert-paiement-fees-no-valid.html", maps,
            new HashMap<>(),EmailContext.NOTIF_EXPERT_PAYMENT_FEES_VALID);
      emailService.sendMail(emailDto, destination);
  }

public String payWithPromotionCode(UserFeesDto paymentDto) {
	boolean ispromotionCodeValid = promotionService.promotionCodeIsValid(paymentDto.getPromotionCode(), TransactionType.USER_FEES);
	if(ispromotionCodeValid) {
		InformationGiver user = (InformationGiver) userService.getCurrentUser();
	    if (!userFeesRepository.findByUserUuid(user.getUuid()).isPresent()) {
	      UserFees userFees = new UserFees();
	      userFees.setPaymentStatus(PaymentStatus.VALIDATED);
	      userFees.setPaymentType(paymentDto.getPaymentType());
	      userFees.setUser(user);
	      userFees.setAmountPaid(registrationFee);
	      userFees.setDatePaiement(new Date());
	      userFees.setFeesType(FeesType.REGISTRATION);
	      userFeesRepository.save(userFees);
	      
	   // send email to expert
	      List<String> destination = Arrays.asList(user.getUserEmail());
	      Map<String, Object> maps = new HashMap<>();

	      EmailDto emailDto =
	          new EmailDto(
	              Constants.MAIL_SUBJECT_EXPERT_PAYMENT_FEE_VALID, "notif-expert-paiement-fees-valid.html", maps,
	              new HashMap<>(),EmailContext.NOTIF_EXPERT_PAYMENT_FEES_VALID);

	      emailService.sendMail(emailDto, destination);
	      
	      return Constants.OK;
	    }
	    return Constants.CONFLICT;
	}
	return Constants.PROMOTIONAL_CODE_NO_VALID;
}

}


