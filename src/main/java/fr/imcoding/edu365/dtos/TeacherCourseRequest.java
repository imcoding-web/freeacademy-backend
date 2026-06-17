package fr.imcoding.edu365.dtos;

import fr.imcoding.edu365.enumeration.CourseType;
import fr.imcoding.edu365.enumeration.Quarter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Rokaya
 * @Date 07/09/2023
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherCourseRequest {
  private UUID courseUuid;
  private String creatorEmail;
  private String title;
  private String description ;
  private CourseType type;
  private Quarter quarter;
  private List<MultipartFile> files = new ArrayList<>();
  private SkillAreaDto skillArea;
  private SkillDto skill;
  private List<MediaDto> medias;
  private Boolean isPremium;
}
