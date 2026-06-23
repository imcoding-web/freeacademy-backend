package fr.imcoding.edu365.client.dtos.request;

import fr.imcoding.edu365.dtos.SkillAreaDto;
import fr.imcoding.edu365.dtos.SkillDto;
import fr.imcoding.edu365.enumeration.AnnouncementType;
import fr.imcoding.edu365.enumeration.InterventionType;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementRequest {

  private String announcementDescription;
  private String announcementTitle;
  private AnnouncementType announcementType;
  private Date announcementEndAvailableDate;
  private SkillDto skill;
  private SkillAreaDto skillArea;
  private InterventionType interventionType;
  private int hoursNumber;
  private boolean homeService;
  private String homeServiceDetails;


}
