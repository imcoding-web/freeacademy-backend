package fr.imcoding.edu365.business.services;

import fr.imcoding.edu365.persistence.entities.PrestationMeeting;
import fr.imcoding.edu365.persistence.repositories.PrestationMeetingRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Rokaya
 * @Date 08/01/2023
 */
@Service
@RequiredArgsConstructor
public class PrestationMeetingService {
private final PrestationMeetingRepository prestationMeetingRepository;

public PrestationMeeting getPrestationMeetingByPrestationUuid(UUID uuid){
  return prestationMeetingRepository.findByPrestationUuid(uuid).orElse(null);
}


  public PrestationMeeting getPrestationMeetingByPrestationOffer(UUID uuid){
    return prestationMeetingRepository.findByPrestationOfferUuid(uuid).orElse(null);
  }

  public PrestationMeeting addPrestationMeeting(PrestationMeeting prestationMeeting){
    return prestationMeetingRepository.save(prestationMeeting);
  }

}
