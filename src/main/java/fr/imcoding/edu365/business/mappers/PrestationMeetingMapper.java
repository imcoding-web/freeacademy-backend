package fr.imcoding.edu365.business.mappers;

import fr.imcoding.edu365.business.services.PrestationMeetingService;
import fr.imcoding.edu365.client.dtos.response.PrestationMeetingResponse;
import fr.imcoding.edu365.persistence.entities.PrestationMeeting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Rokaya
 * @Date 08/01/2023
 */
@Component
@RequiredArgsConstructor
public class PrestationMeetingMapper {

  public PrestationMeetingResponse toPrestationMeetingResponse(PrestationMeeting prestationMeeting){
    return prestationMeeting!=null ?new PrestationMeetingResponse(prestationMeeting.getUuid(),prestationMeeting.getMeetingJoinUrl(),prestationMeeting.getMeetingId(),prestationMeeting.getMeetingCodeSecret()):null;
  }

}
