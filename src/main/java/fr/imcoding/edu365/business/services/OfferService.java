package fr.imcoding.edu365.business.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import fr.imcoding.edu365.business.mappers.OfferMapper;
import fr.imcoding.edu365.business.services.email.EmailService;
import fr.imcoding.edu365.client.dtos.request.AcceptOfferRequest;
import fr.imcoding.edu365.client.dtos.request.OfferRequest;
import fr.imcoding.edu365.client.dtos.response.HasOfferResponse;
import fr.imcoding.edu365.dtos.EmailDto;
import fr.imcoding.edu365.dtos.OfferResponseDto;
import fr.imcoding.edu365.enumeration.AnnouncementStatus;
import fr.imcoding.edu365.enumeration.EmailContext;
import fr.imcoding.edu365.enumeration.OfferStatus;
import fr.imcoding.edu365.enumeration.RoleCode;
import fr.imcoding.edu365.persistence.entities.Announcement;
import fr.imcoding.edu365.persistence.entities.InformationGiver;
import fr.imcoding.edu365.persistence.entities.InformationSeeker;
import fr.imcoding.edu365.persistence.entities.Offer;
import fr.imcoding.edu365.persistence.entities.Prestation;
import fr.imcoding.edu365.persistence.entities.User;
import fr.imcoding.edu365.persistence.repositories.OfferRepository;
import fr.imcoding.edu365.utils.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Rokaya
 * @Date 21/08/2022
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OfferService {
  @Value("${edu365.payment.vat}")
  private Integer vatValue;

  private final OfferRepository offerRepository;
  private final OfferMapper offerMapper;
  private final UserService userService;
  private final AnnouncementService announcementService;
  private final EmailService emailService;
  private final PrestationService prestationService;

  public Offer saveOffer(Offer offer) {
    return this.offerRepository.save(offer);
  }

  public List<OfferResponseDto> getAnnouncementOffers(UUID uuid) {
    return offerRepository.findByAnnouncementUuidOrderByOfferStatusDesc(uuid).stream().sorted(Comparator
        .comparing(Offer::getCreatedAt).reversed())
        .map(offer -> offerMapper.toOfferResponseDto(offer)).collect(Collectors.toList());
  }

  @Transactional
  public void saveOffer(OfferRequest offerRequest, UUID announcementUuid) {
    InformationGiver user = (InformationGiver) userService.getCurrentUser();
    Announcement announcement = announcementService.getAnnouncementByUUID(announcementUuid);
    Offer offer = new Offer();
    offer.setOfferPrice(offerRequest.getOfferPrice());
    offer.setOfferDescription(offerRequest.getOfferDescription());
    offer.setDeadlineDeliveringCorrection(offerRequest.getDeadlineDeliveringCorrection());
    offer.setExpertStartAvailabilityDate(offerRequest.getExpertStartAvailabilityDate());
    offer.setVideoconferenceDuration(offerRequest.getVideoconferenceDuration());
    offer.setOfferGiver(user);
    offer.setOfferStatus(OfferStatus.PENDING_CUSTOMER_PAYMENT);
    offer.setAnnouncement(announcement);
    offer.setOfferVatPrice(offerRequest.getOfferPrice()+(offerRequest.getOfferPrice()*vatValue/100));
    offer = this.saveOffer(offer);
    announcementService.updateAnnouncementStatus(announcement, AnnouncementStatus.RECEIVED_OFFER);
    Map<String, Object> maps = new HashMap<>();
    List<String> destinations = Stream.of(announcement.getAnnouncementPublisher().getUserEmail())
        .collect(Collectors.toList());
    maps.put("announcementUuid", announcement.getUuid().toString());
    maps.put("announcementTitle", announcement.getAnnouncementTitle());
    maps.put("expertFullName", user.getUserLastName() + " " + user.getUserFirstName());

    sendNotificationEmail(maps, destinations, EmailContext.NOTIF_USER_NEW_OFFER);

  }


  public Offer updateOfferStatus(UUID offerUuid, OfferStatus offerStatus) {
    Offer offer = offerRepository.findByUuid(offerUuid);
    offer.setOfferStatus(offerStatus);
    return offerRepository.save(offer);
  }

  public Offer cancelOffer(UUID offerUuid){
    User user =  userService.getCurrentUser();
    Offer offer = offerRepository.findByUuid(offerUuid);
if(user instanceof InformationGiver){
 offer.setOfferStatus(OfferStatus.CANCELED_BY_EXPERT);
  offerRepository.save(offer);
}else if(user instanceof InformationSeeker) {
  offer.setOfferStatus(OfferStatus.CANCELED_BY_ADVERTISER);
  offerRepository.save(offer);
  Map<String, Object> maps = new HashMap<>();
  List<String> destinations = Stream
      .of(offer.getAnnouncement().getAnnouncementPublisher().getUserEmail())
      .collect(Collectors.toList());
  maps.put("announcementTitle", offer.getAnnouncement().getAnnouncementTitle());
  sendNotificationEmail(maps, destinations, EmailContext.NOTIF_EXPERT_DECLINE_OFFER);
}
 return offer;
  }

  public void acceptOffer(AcceptOfferRequest offerRequest) {
    Offer offer = offerRepository.findByUuid(offerRequest.getOfferUuid());
    offer.setOfferStatus(OfferStatus.PENDING_EXPERT_CONFIRMATION);
    offer.setVideoconferenceStartDate(offerRequest.getVideoconferenceStartDate());
    offer.setVideoconferenceStartTime(offerRequest.getVideoconferenceStartTime());
    offer.setWordForExpert(offerRequest.getWordForExpert());
    offerRepository.save(offer);
    Map<String, Object> maps = new HashMap<>();
    maps.put("announcementUuid", offer.getAnnouncement().getUuid().toString());
    maps.put("announcementTitle", offer.getAnnouncement().getAnnouncementTitle());
    sendNotificationEmail(maps,
        Stream.of(offer.getOfferGiver().getUserEmail()).collect(Collectors.toList()),
        EmailContext.NOTIF_EXPERT_ACCEPT_OFFER);
  }


  public List<OfferResponseDto> getAnnouncementsByExpert() {
    User user = userService.getCurrentUser();
    List<OfferResponseDto> result = new ArrayList<>();
    if (user.getUserRole().getRoleCode() == RoleCode.INFORMATION_GIVER) {
      result = offerRepository
          .findByOfferGiverUuidAndOfferStatusNot(user.getUuid(), OfferStatus.PENDING)
          .stream()
          .map(
              offer -> {
                OfferResponseDto dto = offerMapper.toOfferResponseDto(offer);
                return dto;
              })
          .collect(Collectors.toList());
    } else if (user.getUserRole().getRoleCode() == RoleCode.INFORMATION_SEEKER) {
      result = offerRepository
          .findByAnnouncementAnnouncementPublisherAndOfferStatusNot((InformationSeeker) user,
              OfferStatus.PENDING).stream()
          .map(
              offer -> {
                OfferResponseDto dto = offerMapper.toOfferResponseDto(offer);
                return dto;
              })
          .collect(Collectors.toList());
      ;
    }
    return result;
  }

  public OfferResponseDto getOfferByUuid(UUID uuid) {
    return offerMapper.toOfferResponseDto(offerRepository.findByUuid(uuid));
  }

  public List<Offer> getOfferByAnnouncementAndExpert(UUID announcementUuid) {
    InformationGiver user = (InformationGiver) userService.getCurrentUser();

    return offerRepository
        .findByAnnouncementUuidAndOfferGiverUuidAndOfferStatusNot(announcementUuid, user.getUuid(),OfferStatus.CANCELED_BY_EXPERT);
  }
  
	public HasOfferResponse hasOffer(UUID announcementUuid) {
		List<Offer> offers = getOfferByAnnouncementAndExpert(announcementUuid);
		Announcement announcement = announcementService.getAnnouncementByUUID(announcementUuid);
		return HasOfferResponse.builder().announcementInPackage(announcement.isInStudyPackage())
				.hasAnnouncementOffer(offers.isEmpty() ? false : true).estimatedPriceToPay(announcement.getEstimatedPriceToPay()).build();
	}

  public Offer confirmOffer(UUID offerUuid, String wordForAdvertiser) {
    log.info("Confirm Offer with Id: {}", offerUuid);
    Offer offer = offerRepository.findByUuid(offerUuid);
    offer.setOfferStatus(OfferStatus.PENDING_CUSTOMER_PAYMENT);
    offer.setWordForAdvertiser(wordForAdvertiser);
    Map<String, Object> maps = new HashMap<>();
    List<String> destinations = Stream.of(offer.getAnnouncement().getAnnouncementPublisher().getUserEmail())
        .collect(Collectors.toList());
    maps.put("announcementUuid", offer.getAnnouncement().getUuid().toString());
    maps.put("announcementTitle", offer.getAnnouncement().getAnnouncementTitle());
    maps.put("expertFullName", offer.getOfferGiver().getUserLastName() + " " + offer.getOfferGiver().getUserFirstName());
    offerRepository.save(offer);
    sendNotificationEmail(maps, destinations, EmailContext.NOTIF_USER_OFFER_VALID);
    return offer;

  }

  public Offer refuseOffer(UUID offerUuid, String cancelingReason) {
    User user=userService.getCurrentUser();
    log.info("Refuse Offer with Id: {}", offerUuid);
    Offer offer = offerRepository.findByUuid(offerUuid);
    if(user.getUserRole().getRoleCode().equals(RoleCode.INFORMATION_GIVER)) {
      offer.setOfferStatus(OfferStatus.REFUSED_BY_EXPERT);
      Map<String, Object> maps = new HashMap<>();
      List<String> destinations = Stream.of(offer.getAnnouncement().getAnnouncementPublisher().getUserEmail())
          .collect(Collectors.toList());
      maps.put("announcementUuid", offer.getAnnouncement().getUuid().toString());
      maps.put("announcementTitle", offer.getAnnouncement().getAnnouncementTitle());
      maps.put("expertFullName", offer.getOfferGiver().getUserLastName() + " " + offer.getOfferGiver().getUserFirstName());
      offerRepository.save(offer);
      sendNotificationEmail(maps, destinations, EmailContext.NOTIF_USER_OFFER_NOT_VALID);

    }else  if(user.getUserRole().getRoleCode().equals(RoleCode.INFORMATION_SEEKER)) {
      offer.setOfferStatus(OfferStatus.REFUSED);
    }
    offer.setCancelingReason(cancelingReason);
    return offerRepository.save(offer);
  }


  public void sendNotificationEmail(Map<String, Object> maps, List<String> destinations,
      EmailContext emailContext) {
    EmailDto emailDto =
        new EmailDto();
    if (emailContext == EmailContext.NOTIF_USER_NEW_OFFER) {
      emailDto =
          new EmailDto(
              Constants.MAIL_SUBJECT_USER_NEW_OFFER, "notif-user-new-offer.html", maps,
              new HashMap<>(),EmailContext.NOTIF_USER_NEW_OFFER);
    } else if (emailContext == EmailContext.NOTIF_EXPERT_ACCEPT_OFFER) {

      emailDto =
          new EmailDto(
              Constants.MAIL_SUBJECT_EXPERT_OFFER_ACCEPTED, "notif-expert-accept-offer.html", maps,
              new HashMap<>(),EmailContext.NOTIF_EXPERT_ACCEPT_OFFER);
    }
    else if (emailContext == EmailContext.NOTIF_EXPERT_DECLINE_OFFER) {

      emailDto =
          new EmailDto(
              Constants.MAIL_SUBJECT_EXPERT_OFFER_DECLINED, "notif-expert-decline-offer.html", maps,
              new HashMap<>(),EmailContext.NOTIF_EXPERT_DECLINE_OFFER);
    }
    else if (emailContext == EmailContext.NOTIF_USER_OFFER_VALID) {

      emailDto =
          new EmailDto(
              Constants.MAIL_SUBJECT_USER_OFFER_ACCEPTED, "notif-user-offer-valid.html", maps,
              new HashMap<>(),EmailContext.NOTIF_USER_OFFER_VALID);
    }
    else if (emailContext == EmailContext.NOTIF_USER_OFFER_NOT_VALID) {

      emailDto =
          new EmailDto(
              Constants.MAIL_SUBJECT_USER_OFFER_NOT_ACCEPTED, "notif-user-offer-not-valid.html", maps,
              new HashMap<>(),EmailContext.NOTIF_USER_OFFER_NOT_VALID);
    }

    emailService.sendMail(emailDto, destinations);
  }


  public List<OfferResponseDto> getExpertOffers(){
    InformationGiver user = (InformationGiver) userService.getCurrentUser();
    return offerRepository.findByOfferGiverUuidOrderByCreatedAtDesc(user.getUuid()).stream().map(offerMapper::toOfferResponseDto).collect(Collectors.toList());
  }

	public List<OfferResponseDto> getClientOffers() {
		InformationSeeker user = (InformationSeeker) userService.getCurrentUser();
		return offerRepository.findByAnnouncementAnnouncementPublisherUuid(user.getUuid()).stream()
				.sorted(Comparator.comparing(Offer::getCreatedAt).reversed()).map(offerMapper::toOfferResponseDto)
				.collect(Collectors.toList());
	}

	public Offer getOfferById(String orderId) {
		return offerRepository.findByUniqueIdentifier(orderId);
	}

	public void acceptOfferInPackage(UUID offerUuid) {
		Offer offer = offerRepository.findByUuid(offerUuid);
		offer.setOfferStatus(OfferStatus.ACCEPTED_IN_PACKAGE);
		saveOffer(offer);
		Prestation prestation = new Prestation();
		prestation.setOffer(offer);
		prestationService.addPrestation(prestation);

		// send email to expert
		List<String> destination = Arrays.asList(offer.getOfferGiver().getUserEmail());
		Map<String, Object> maps = new HashMap<>();
		maps.put("userEmail", offer.getAnnouncement().getAnnouncementPublisher().getUserEmail());
		maps.put("announcementTitle", offer.getAnnouncement().getAnnouncementTitle());
		maps.put("userFullName", offer.getAnnouncement().getAnnouncementPublisher().getUserLastName() + " "
				+ offer.getAnnouncement().getAnnouncementPublisher().getUserFirstName());
		maps.put("userPhoneNumber", offer.getAnnouncement().getAnnouncementPublisher().getUserPhoneNumber());

		EmailDto emailDto = new EmailDto(Constants.MAIL_SUBJECT_EXPERT_PAYMENT_VALID,
				"notif-expert-paiement-valid.html", maps, new HashMap<>(), EmailContext.NOTIF_EXPERT_PAYMENT_VALID);

		emailService.sendMail(emailDto, destination);

		// send email to student

		destination = Arrays.asList(offer.getAnnouncement().getAnnouncementPublisher().getUserEmail());
		maps = new HashMap<>();
		maps.put("expertEmail", offer.getOfferGiver().getUserEmail());
		maps.put("expertFullName",
				offer.getOfferGiver().getUserLastName() + " " + offer.getOfferGiver().getUserFirstName());
		maps.put("expertPhoneNumber", offer.getOfferGiver().getUserPhoneNumber());

		emailDto = new EmailDto(Constants.MAIL_SUBJECT_USER_PAYMENT_VALID, "notif-user-paiement-valid.html", maps,
				new HashMap<>(), EmailContext.NOTIF_USER_PAYMENT_VALID);

		emailService.sendMail(emailDto, destination);

	}
}
