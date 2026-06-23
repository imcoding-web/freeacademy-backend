package fr.imcoding.edu365.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import fr.imcoding.edu365.client.dtos.response.UserResponse;
import fr.imcoding.edu365.enumeration.AnnouncementStatus;
import fr.imcoding.edu365.enumeration.AnnouncementType;
import fr.imcoding.edu365.enumeration.InterventionType;
import fr.imcoding.edu365.utils.Constants;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 25/07/2022
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementDto {
  private UUID announcementUuid;
  private String announcementDescription;
  private String announcementTitle;
  private AnnouncementType announcementType;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constants.DEFAULT_TIMEZONE)
  private Date announcementEndAvailableDate;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constants.DEFAULT_TIMEZONE)
  private Date announcementCreatedAt;
  private SkillDto skill;
  private SkillAreaDto skillArea;
  private InterventionType interventionType;
  private List<MediaDetails> medias;
  private UserResponse announcementPublisher;
  private int hoursNumber;
  private AnnouncementStatus announcementStatus;
  private boolean homeService;
  private String homeServiceDetails;
  private int estimatedHourNumber;





}
