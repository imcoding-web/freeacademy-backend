package fr.imcoding.edu365.client.dtos.response;

import fr.imcoding.edu365.dtos.MediaDto;
import fr.imcoding.edu365.dtos.SkillDto;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 08/06/2022
 */
  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public class ProjectDetails {
    private UUID projectUuid;
    private String title;
    private String description;
    private SkillDto skill;
    private List<MediaDto> medias;

  }


