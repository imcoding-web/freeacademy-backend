package fr.imcoding.edu365.dtos;

import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementSearchCriteria {
  private List<String> skillLevels;
  private List<String> skills;
  private List<String> interventionTypes;
  private List<String> announcementTypes;
  private String endAvailableDate;



  public Boolean isEmptySkillLevels() {
    return CollectionUtils.isEmpty(skillLevels);
  }

  public Boolean isEmptySkills() {
    return CollectionUtils.isEmpty(skills);
  }
  public Boolean isEmptyInterventionTypes() {
    return CollectionUtils.isEmpty(interventionTypes);
  }
  public Boolean isEmptyAnnouncementType() {
    return CollectionUtils.isEmpty(announcementTypes);
  }



}
