package fr.imcoding.edu365.client.dtos.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.imcoding.edu365.dtos.MediaDetails;
import fr.imcoding.edu365.dtos.UserDto;
import fr.imcoding.edu365.utils.Constants;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 30/07/2022
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementFiltredResponseDto {

  @JsonProperty("announcementUuid")
  private UUID announcementUuid;

  @JsonProperty("announcementTitle")
  private String announcementTitle;

  @JsonProperty("announcementMedias")
  private List<MediaDetails> announcementMedias;

  @JsonProperty("announcementDescription")
  private String announcementDescription;

  @JsonProperty("announcementIntervetionType")
  private String announcementIntervetionType;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = Constants.DEFAULT_TIMEZONE)
  private Date announcementEndAvailableDate;

  private UserResponse announcementPublisher;

  @JsonProperty("skillLevel")
  private String skillLevel;

  @JsonProperty("skill")
  private String skill;

  @JsonProperty("announcementType")
  private String announcementType;
  private boolean proposedOffer;
  private boolean homeService;
  private boolean inStudyPackage;
  private int estimatedPriceToPay;


}
