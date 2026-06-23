package fr.imcoding.edu365.client.dtos.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import fr.imcoding.edu365.dtos.MediaDto;
import fr.imcoding.edu365.dtos.SkillAreaDto;
import fr.imcoding.edu365.dtos.SkillDto;
import fr.imcoding.edu365.dtos.UserDto;
import fr.imcoding.edu365.enumeration.AnnouncementStatus;
import fr.imcoding.edu365.enumeration.AnnouncementType;
import fr.imcoding.edu365.enumeration.InterventionType;
import fr.imcoding.edu365.persistence.entities.Skill;
import fr.imcoding.edu365.persistence.entities.SkillArea;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyAnnouncementResponse {

  private UUID announcementUuid;
  private String announcementTitle;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Africa/Tunis")
  private Date announcementCreatedDate;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Africa/Tunis")

  private Integer announcementNumberLike = 0;
  private Integer announcementNumberDislike = 0;

  private AnnouncementType announcementType;

  private UserDto announcementOwnerPublished;
  private String announcementMainPictureUrl;
  private AnnouncementStatus announcementStatus;
  private Date announcementEndAvailableDate;
  private SkillAreaDto announcementSkillArea;
  private SkillDto skill;
  private InterventionType interventionType;
  private List<MediaDto> medias;
  private Integer offerNumber = 0;

 // private List<OfferResponse> offers;


}
