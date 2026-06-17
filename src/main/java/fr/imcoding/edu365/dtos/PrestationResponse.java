package fr.imcoding.edu365.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import fr.imcoding.edu365.client.dtos.response.PrestationMeetingResponse;
import fr.imcoding.edu365.enumeration.PrestationStatus;
import fr.imcoding.edu365.persistence.entities.PrestationMeeting;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 24/10/2022
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrestationResponse {
  private UUID prestationUuid;
  private PrestationStatus prestationStatus;
  private OfferResponseDto offer;
  private List<MediaDetails> medias;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
  private Date markAsSolvedDate;

  private PrestationMeetingResponse prestationMeeting;


}
