package fr.imcoding.edu365.client.dtos.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import fr.imcoding.edu365.dtos.UserDto;
import fr.imcoding.edu365.enumeration.AnnouncementType;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementDetailsResponse {

  private UUID announcementUuid;
  private String announcementTitle;
  private String announcementDescription;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Africa/Tunis")
  private Date announcementCreatedDate;

  private String announcementSummary;
  private String announcementAdditionalInformations;
  private AnnouncementType announcementType;

  private UserDto announcementPublisher;
  private List<String> announcementPictureUrls;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Africa/Tunis")
  private Date announcementEndAvailableDate;

}
