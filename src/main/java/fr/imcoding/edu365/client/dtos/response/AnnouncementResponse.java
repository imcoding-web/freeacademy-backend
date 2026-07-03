package fr.imcoding.edu365.client.dtos.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import fr.imcoding.edu365.dtos.UserDto;
import fr.imcoding.edu365.enumeration.AnnouncementType;
import java.util.Date;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementResponse {

  private UUID announcementUuid;
  private String announcementTitle;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Africa/Tunis")
  private Date announcementCreatedDate;

  private Integer announcementNumberLike = 0;
  private Integer announcementNumberDislike = 0;

  private AnnouncementType announcementType;

  private UserDto announcementPublisher;
  private String announcementMainPictureUrl;

}
