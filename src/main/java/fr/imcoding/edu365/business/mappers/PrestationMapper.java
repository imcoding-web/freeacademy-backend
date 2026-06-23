package fr.imcoding.edu365.business.mappers;

import fr.imcoding.edu365.business.services.PrestationMeetingService;
import fr.imcoding.edu365.client.dtos.request.PrestationRequest;
import fr.imcoding.edu365.dtos.OfferResponseDto;
import fr.imcoding.edu365.dtos.PrestationResponse;
import fr.imcoding.edu365.persistence.entities.Prestation;
import fr.imcoding.edu365.persistence.entities.PrestationMeeting;
import fr.imcoding.edu365.persistence.repositories.PrestationRepository;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Rokaya
 * @Date 24/10/2022
 */
@Component
@RequiredArgsConstructor
public class PrestationMapper {
  private final OfferMapper offerMapper;
  private final MediaDatailsMapper mediaMapper;
  private final PrestationRepository prestationRepository;
  private final PrestationMeetingService prestationMeetingService;
  private final PrestationMeetingMapper prestationMeetingMapper;



  public PrestationResponse toPrestationResponse(Prestation prestation){
    PrestationMeeting prestationMeeting=prestationMeetingService.getPrestationMeetingByPrestationUuid(prestation.getUuid());
    PrestationResponse response =  PrestationResponse.builder()
        .prestationUuid(prestation.getUuid())
            .date(prestation.getDate())
        .live(prestation.isLive())
        .prestationStatus(prestation.getPrestationStatus())
        .offer(offerMapper.toOfferPrestationResponseDto(offerMapper.toPrestationDetailsOfferResponseDto(prestation.getOffer()))).markAsSolvedDate(prestation.getMarkAsSolvedDate())
        .medias(!prestation.getMedias().isEmpty()? prestation.getMedias().stream().map(media->mediaMapper.toMediaDetails(media)).collect(
            Collectors.toList()):null).prestationMeeting(prestationMeetingMapper.toPrestationMeetingResponse(prestationMeeting)).build();

    return response;

  }
  public PrestationResponse toPrestationDetailsResponse(Prestation prestation){
    PrestationMeeting prestationMeeting=prestationMeetingService.getPrestationMeetingByPrestationUuid(prestation.getUuid());
    return PrestationResponse.builder()
        .prestationUuid(prestation.getUuid())
        .prestationStatus(prestation.getPrestationStatus())
        .offer(offerMapper.toPrestationDetailsOfferResponseDto(prestation.getOffer())).markAsSolvedDate(prestation.getMarkAsSolvedDate())
        .medias(!prestation.getMedias().isEmpty()? prestation.getMedias().stream().map(media->mediaMapper.toMediaDetails(media)).collect(
            Collectors.toList()):null).prestationMeeting(prestationMeetingMapper.toPrestationMeetingResponse(prestationMeeting)).build();

  }


  public Prestation toPrestation(PrestationRequest prestationRequest) {
    return prestationRepository.findByUuid(prestationRequest.getPrestationUuid()).orElse(null);
  }

}
