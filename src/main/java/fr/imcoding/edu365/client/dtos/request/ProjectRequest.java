package fr.imcoding.edu365.client.dtos.request;

import fr.imcoding.edu365.dtos.MediaDto;
import fr.imcoding.edu365.dtos.SkillDto;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Rokaya
 * @Date 05/06/2022
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectRequest {
  private UUID projectUuid;
  private String title;
  private String description;
  private SkillDto skill;
  private List<MultipartFile> files;
  private List<MediaDto> medias;


}
