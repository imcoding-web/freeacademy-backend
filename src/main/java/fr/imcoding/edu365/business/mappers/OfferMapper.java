package fr.imcoding.edu365.business.mappers;

import fr.imcoding.edu365.business.services.PrestationMeetingService;
import fr.imcoding.edu365.client.dtos.response.OfferResponse;
import fr.imcoding.edu365.dtos.OfferResponseDto;
import fr.imcoding.edu365.dtos.SkillAreaDto;
import fr.imcoding.edu365.persistence.entities.Offer;
import fr.imcoding.edu365.persistence.repositories.OfferRepository;
import fr.imcoding.edu365.utils.DatesUtils;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Rokaya
 * @Date 21/08/2022
 */
@Component
@RequiredArgsConstructor
public class OfferMapper {
  private final ExpertOfferDetailsMapper expertMapper;
  private final UserMapper userMapper;
  private final OfferRepository offerRepository;
  private final SkillAreaMapper skillAreaMapper;
  private final SkillMapper skillMapper;
  private final MediaDatailsMapper mediaDatailsMapper;
  private final PrestationMeetingService prestationMeetingService;
  private final PrestationMeetingMapper prestationMeetingMapper;







  public OfferResponse toOfferResponse(Offer offer){
    return new OfferResponse(offer.getUuid(),offer.getOfferPrice(),
        offer.getOfferDescription(),
        offer.getDeadlineDeliveringCorrection(),
        offer.getExpertStartAvailabilityDate(),
        offer.getVideoconferenceDuration(),
        expertMapper.toExpertDetailsForOffer(offer.getOfferGiver()),offer.getOfferStatus(),
        offer.getWordForAdvertiser(),offer.getAnnouncement().getUuid(),offer.getAnnouncement().getAnnouncementTitle());
  }



  public OfferResponseDto toOfferResponseDto(Offer offer){
    return  OfferResponseDto.builder().offerUuid(
        offer.getUuid()).videoconferenceDate(
        (offer.getVideoconferenceStartDate()!=null&& offer.getVideoconferenceStartTime()!=null)?DatesUtils.setDateTime(offer.getVideoconferenceStartDate(),offer.getVideoconferenceStartTime()):null).
        correctionDeliveryDate(offer.getDeadlineDeliveringCorrection()!=null?offer.getDeadlineDeliveringCorrection():null).
        offerExpert(expertMapper.toExpertDetailsForOffer(offer.getOfferGiver())).
        offerClient(userMapper.toUserDetails(offer.getAnnouncement().getAnnouncementPublisher())).
        offerStatus(offer.getOfferStatus()).
        wordForExpert(offer.getWordForExpert()).
        offerPrice(offer.getOfferPrice()).
        offerDescription(offer.getOfferDescription()).
        announcementUuid(offer.getAnnouncement().getUuid()).
        announcementInterventionType(offer.getAnnouncement().getInterventionType()).
        announcementType(offer.getAnnouncement().getAnnouncementType()).
        announcementTitle(offer.getAnnouncement().getAnnouncementTitle()).
        videoconferenceDuration(offer.getVideoconferenceDuration()).
        deadlineDeliveringCorrection(offer.getDeadlineDeliveringCorrection()).
        wordForAdvertiser(offer.getWordForAdvertiser()).
        expertStartAvailabilityDate( offer.getExpertStartAvailabilityDate()).
        hoursNumber(offer.getAnnouncement().getHoursNumber()).
        uniqueIdentifier(offer.getUniqueIdentifier()).offerVatprice(offer.getOfferVatPrice()).offerPriceToPay(offer.getOfferPriceToPay()).meetingResponse(prestationMeetingService.getPrestationMeetingByPrestationOffer(offer.getUuid())!=null?prestationMeetingMapper.toPrestationMeetingResponse(prestationMeetingService.getPrestationMeetingByPrestationOffer(offer.getUuid())):null)
        .inStudyPackage(offer.getAnnouncement().isInStudyPackage())
        .build();
  }
  public Offer toOffer(OfferResponseDto offerResponseDto) {
    return offerResponseDto != null ? offerRepository.findByUuid(offerResponseDto.getOfferUuid()) : null;
  }

  public OfferResponseDto toOfferPrestationResponseDto(OfferResponseDto offerResponseDto){
    return OfferResponseDto.builder().offerExpert(offerResponseDto.getOfferExpert()).correctionDeliveryDate(offerResponseDto.getCorrectionDeliveryDate()).videoconferenceDate(offerResponseDto.getVideoconferenceDate()).announcementUuid(offerResponseDto.getAnnouncementUuid()).videoconferenceDuration(offerResponseDto.getVideoconferenceDuration()).build();

  }
  public OfferResponseDto toOfferPaymentResponseDto(OfferResponseDto offerResponseDto){
    return OfferResponseDto.builder().uniqueIdentifier(offerResponseDto.getUniqueIdentifier()).offerExpert(offerResponseDto.getOfferExpert()).offerClient(offerResponseDto.getOfferClient()).offerVatprice(offerResponseDto.getOfferVatprice()).offerPriceToPay(offerResponseDto.getOfferPriceToPay()).announcementUuid(offerResponseDto.getAnnouncementUuid()).announcementTitle(offerResponseDto.getAnnouncementTitle()).build();

  }

  public OfferResponseDto toPrestationDetailsOfferResponseDto(Offer offer){
    return  OfferResponseDto.builder().offerUuid(
        offer.getUuid()).videoconferenceDate(
        (offer.getVideoconferenceStartDate()!=null&& offer.getVideoconferenceStartTime()!=null)?DatesUtils.setDateTime(offer.getVideoconferenceStartDate(),offer.getVideoconferenceStartTime()):null).
        correctionDeliveryDate(offer.getDeadlineDeliveringCorrection()!=null?offer.getDeadlineDeliveringCorrection():null).
        offerExpert(expertMapper.toExpertDetailsForOffer(offer.getOfferGiver())).
        offerClient(userMapper.toUserDetails(offer.getAnnouncement().getAnnouncementPublisher())).
        offerStatus(offer.getOfferStatus()).
        wordForExpert(offer.getWordForExpert()).
        offerPrice(offer.getOfferPrice()).
        announcementUuid(offer.getAnnouncement().getUuid()).
        announcementInterventionType(offer.getAnnouncement().getInterventionType()).
        announcementType(offer.getAnnouncement().getAnnouncementType()).
        announcementTitle(offer.getAnnouncement().getAnnouncementTitle()).
        announcementDescription(offer.getAnnouncement().getAnnouncementDescription()).
        announcementSkillLevel(skillAreaMapper.toSkillAreaDto(offer.getAnnouncement().getAnnouncementSkillArea())).
        announcementSkill(skillMapper.toSkillDto(offer.getAnnouncement().getSkill())).
        videoconferenceDuration(offer.getVideoconferenceDuration()).
        announcementMedias((offer.getAnnouncement().getMedias()!=null)?offer.getAnnouncement().getMedias().stream().map(mediaDatailsMapper::toMediaDetails).collect(Collectors.toList()):null).
        build();
  }
}