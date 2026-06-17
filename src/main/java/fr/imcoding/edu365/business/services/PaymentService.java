package fr.imcoding.edu365.business.services;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import fr.imcoding.edu365.business.mappers.OfferMapper;
import fr.imcoding.edu365.business.mappers.PaymentMapper;
import fr.imcoding.edu365.business.services.email.EmailService;
import fr.imcoding.edu365.client.dtos.response.ExpertPaymentResponse;
import fr.imcoding.edu365.client.dtos.response.PaymentResponse;
import fr.imcoding.edu365.dtos.EmailDto;
import fr.imcoding.edu365.dtos.MessageRequestDto;
import fr.imcoding.edu365.dtos.OfferResponseDto;
import fr.imcoding.edu365.dtos.PaymentDto;
import fr.imcoding.edu365.dtos.PaymentSearchRequest;
import fr.imcoding.edu365.enumeration.EmailContext;
import fr.imcoding.edu365.enumeration.MediaContext;
import fr.imcoding.edu365.enumeration.OfferStatus;
import fr.imcoding.edu365.enumeration.PaymentStatus;
import fr.imcoding.edu365.enumeration.PaymentType;
import fr.imcoding.edu365.persistence.entities.Media;
import fr.imcoding.edu365.persistence.entities.Offer;
import fr.imcoding.edu365.persistence.entities.Payment;
import fr.imcoding.edu365.persistence.entities.Prestation;
import fr.imcoding.edu365.persistence.repositories.PaymentRepository;
import fr.imcoding.edu365.persistence.specifications.PaymentSpecifications;
import fr.imcoding.edu365.utils.Constants;
import lombok.RequiredArgsConstructor;

/**
 * @author Rokaya
 * @Date 05/10/2022
 */
@Service
@RequiredArgsConstructor
public class PaymentService {
  private final PaymentRepository paymentRepository;
  private final MediaService mediaService;
  private final OfferService offerService;
  private final OfferMapper offerMapper;
  private final UserService userService;
  private final PaymentMapper paymentMapper;
  private final PrestationService prestationService;
  private final EmailService emailService;
  private final UserBankDataService userBankDataService;




  @Transactional
  public String payOffer(PaymentDto paymentDto){
    
   Offer offer= offerMapper.toOffer(offerService.getOfferByUuid(paymentDto.getOfferUuid()));
   if(offer.getOfferStatus() == OfferStatus.PENDING_CUSTOMER_PAYMENT) {
	   Payment payment=new Payment();
	    payment.setOffer(offer);
	    payment.setPaymentStatus(PaymentStatus.PENDING_VALIDATION);
	    payment.setPaymentType(paymentDto.getPaymentType());
	    if(paymentDto.getPaymentMedia()!=null){
	      try {
	        Media media = mediaService.saveMedia(paymentDto.getPaymentMedia(), MediaContext.PAYMENT_DOCUMENT);
	        payment.setPaymentMedia(media);
	      } catch (Exception e) {
	        e.printStackTrace();
	      }
	    }
	    offerService.updateOfferStatus(offer.getUuid(),OfferStatus.PENDING_PAYMENT_VALIDATION);
	    Prestation prestation=new Prestation();
	    prestation.setOffer(offer);
	   //prestationService.addPrestation(prestation);
	    paymentRepository.save(payment); 
	    return Constants.OK;
   }
   return Constants.CONFLICT;

  }

  public List<PaymentResponse> getOfferByUser(){
    return this.paymentRepository.findByOfferAnnouncementAnnouncementPublisherUuid(userService.getCurrentUser().getUuid()).stream().sorted(Comparator
        .comparing(Payment::getCreatedAt).reversed()).map(paymentMapper::toPaymentResponse).collect(
        Collectors.toList());
  }


  public List<OfferResponseDto> getExpertPrestations(){
    return this.paymentRepository.findByOfferOfferGiverUuidAndPaymentStatus(userService.getCurrentUser().getUuid(),PaymentStatus.VALIDATED).stream().map(payment -> {
      return offerMapper.toOfferResponseDto(payment.getOffer());
    }).collect(Collectors.toList());
  }

  public List<PaymentResponse> getPaymentByStatus(PaymentStatus paymentStatus){
    return this.paymentRepository.findByPaymentStatus(paymentStatus).stream().map(paymentMapper::toPaymentResponse).collect(
        Collectors.toList());
  }

  public PaymentResponse getPaymentByUuid(UUID paymentUuid){
    return paymentMapper.toPaymentResponse(this.paymentRepository.findByUuid(paymentUuid).orElse(null));
  }

  public void validate(UUID paymentUuid) {
    Payment payment = paymentRepository.findByUuid(paymentUuid).orElse(null);
    payment.setPaymentStatus(PaymentStatus.VALIDATED);
    paymentRepository.save(payment);
    payment.getOffer().setOfferStatus(OfferStatus.VALIDATED_AND_PAID);
    offerService.saveOffer(payment.getOffer());
    //userBankDataService.updateUserBankData(new UserBankDataDto(payment.getOffer().getOfferGiver().getUuid(),payment.getOffer().getOfferPrice()));
    Prestation prestation=new Prestation();
    prestation.setOffer(payment.getOffer());
    prestationService.addPrestation(prestation);


    // send email to expert
    List<String> destination = Arrays.asList(payment.getOffer().getOfferGiver().getUserEmail());
    Map<String, Object> maps = new HashMap<>();
    maps.put("userEmail", payment.getOffer().getAnnouncement().getAnnouncementPublisher().getUserEmail());
    maps.put("announcementTitle", payment.getOffer().getAnnouncement().getAnnouncementTitle());
    maps.put("userFullName", payment.getOffer().getAnnouncement().getAnnouncementPublisher().getUserLastName() + " " + payment.getOffer().getAnnouncement().getAnnouncementPublisher().getUserFirstName());
    maps.put("userPhoneNumber", payment.getOffer().getAnnouncement().getAnnouncementPublisher().getUserPhoneNumber());

    EmailDto emailDto =
        new EmailDto(
            Constants.MAIL_SUBJECT_EXPERT_PAYMENT_VALID, "notif-expert-paiement-valid.html", maps,
            new HashMap<>(),EmailContext.NOTIF_EXPERT_PAYMENT_VALID);

    emailService.sendMail(emailDto, destination);

// send email to student

    destination = Arrays.asList(payment.getOffer().getAnnouncement().getAnnouncementPublisher().getUserEmail());
    maps = new HashMap<>();
    maps.put("expertEmail", payment.getOffer().getOfferGiver().getUserEmail());
    maps.put("expertFullName", payment.getOffer().getOfferGiver().getUserLastName() + " " + payment.getOffer().getOfferGiver().getUserFirstName());
    maps.put("expertPhoneNumber",payment.getOffer().getOfferGiver().getUserPhoneNumber());

    emailDto =
        new EmailDto(
            Constants.MAIL_SUBJECT_USER_PAYMENT_VALID, "notif-user-paiement-valid.html", maps,
            new HashMap<>(),EmailContext.NOTIF_USER_PAYMENT_VALID);

    emailService.sendMail(emailDto, destination);
  }





  public List<PaymentResponse> getFilteredPayment(String startDate,String endDate,String paymentType,String expertFirstName,String expertLastName){

    PaymentSearchRequest paymentSearchRequest =
        new PaymentSearchRequest(startDate,endDate,paymentType,expertFirstName,expertLastName);
    List<Payment> payments = null;
    if (paymentSearchRequest.getStartDate()==null && paymentSearchRequest.getEndDate()==null &&
        paymentSearchRequest.getPaymentType()==null && paymentSearchRequest.getExpertFirstName()==null
        ) {
      payments = paymentRepository.findAll();
    } else {
      payments =
          paymentRepository.findAll(
              PaymentSpecifications
                  .createAnnouncementSpecifications(paymentSearchRequest));

    }

return payments.stream().map(paymentMapper::toPaymentResponse).collect(Collectors.toList());



   // return paymentRepository.findAllByCreatedAtGreaterThanEqualOrCreatedAtLessThanEqualOrPaymentType(startDate,endDate,paymentType);

  }


  public void refuse(UUID uuid,MessageRequestDto messageRequest) {
    Payment payment = paymentRepository.findByUuid(uuid).orElse(null);
    payment.setPaymentStatus(PaymentStatus.REFUSED);
    payment.setRefusalReason(messageRequest.getMessage());
    paymentRepository.save(payment);
    // send email
   /* List<String> destination = Arrays.asList(announcement.getAnnouncementPublisher().getUserEmail());
    Map<String, Object> maps = new HashMap<>();
    maps.put("announcementUuid", announcement.getUuid().toString());
    maps.put("refusalReason", messageRequest.getMessage());
    if (destination != null && !destination.isEmpty()) {
      sendNotificationEmail(maps, destination,EmailContext.NOTIF_USER_ANNOUNCEMENT_REFUSED);
    }*/
  }

  public List<ExpertPaymentResponse> getAllPayments(PaymentStatus paymentStatus){
    return this.paymentRepository.findByPaymentStatus(paymentStatus).stream().map(payment -> {
      return paymentMapper.toExpertPaymentResponse(payment.getOffer().getOfferGiver());
    }).collect(Collectors.toList());
  }

  public void savePaymentOperation(Offer offer,PaymentType paymentType){
    Payment payment=new Payment();
    payment.setPaymentStatus(PaymentStatus.VALIDATED);
    payment.setOffer(offer);
    payment.setPaymentType(paymentType);
    paymentRepository.save(payment);
    offer.setOfferStatus(OfferStatus.VALIDATED_AND_PAID);
    offerService.saveOffer(offer);
    Prestation prestation=new Prestation();
    prestation.setOffer(payment.getOffer());
    prestationService.addPrestation(prestation);


    //userBankDataService.updateUserBankData(new UserBankDataDto(offer.getOfferGiver().getUuid(),offer.getOfferVatPrice()));

    // send email to expert
    List<String> destination = Arrays.asList(offer.getOfferGiver().getUserEmail());
    Map<String, Object> maps = new HashMap<>();
    maps.put("userEmail", offer.getAnnouncement().getAnnouncementPublisher().getUserEmail());
    maps.put("announcementTitle", offer.getAnnouncement().getAnnouncementTitle());
    maps.put("userFullName", offer.getAnnouncement().getAnnouncementPublisher().getUserLastName() + " " + offer.getAnnouncement().getAnnouncementPublisher().getUserFirstName());
    maps.put("userPhoneNumber",offer.getAnnouncement().getAnnouncementPublisher().getUserPhoneNumber());

    EmailDto emailDto =
        new EmailDto(
            Constants.MAIL_SUBJECT_EXPERT_PAYMENT_VALID, "notif-expert-paiement-valid.html", maps,
            new HashMap<>(),EmailContext.NOTIF_EXPERT_PAYMENT_VALID);

    emailService.sendMail(emailDto, destination);
    // send email to student

    destination = Arrays.asList(offer.getAnnouncement().getAnnouncementPublisher().getUserEmail());
    maps = new HashMap<>();
    maps.put("expertEmail", offer.getOfferGiver().getUserEmail());
    maps.put("expertFullName", offer.getOfferGiver().getUserLastName() + " " + offer.getOfferGiver().getUserFirstName());
    maps.put("expertPhoneNumber",offer.getOfferGiver().getUserPhoneNumber());

    emailDto =
        new EmailDto(
            Constants.MAIL_SUBJECT_USER_PAYMENT_VALID, "notif-user-paiement-valid.html", maps,
            new HashMap<>(),EmailContext.NOTIF_USER_PAYMENT_VALID);

    emailService.sendMail(emailDto, destination);


    //send email to all administrator

  }

}
