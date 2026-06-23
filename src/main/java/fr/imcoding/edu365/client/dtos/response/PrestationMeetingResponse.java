package fr.imcoding.edu365.client.dtos.response;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 08/01/2023
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrestationMeetingResponse {
  private UUID prestationMeetingUuid;
  private String meetingJoinUrl;
  private Long meetingId;
  private String meetingCodeSecret;
}
