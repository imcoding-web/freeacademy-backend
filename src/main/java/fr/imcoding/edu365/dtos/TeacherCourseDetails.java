package fr.imcoding.edu365.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import fr.imcoding.edu365.enumeration.CourseType;
import fr.imcoding.edu365.enumeration.Quarter;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 14/09/2023
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(Include.NON_NULL)

public class TeacherCourseDetails {
  private CourseCreatorDto courseCreator;
  private UUID courseUuid;
  private String title;
  private String description;
  private SkillAreaDto skillArea;
  private SkillDto skill;
  private CourseType type;
  private Quarter quarter;
  private List<MediaDetails> medias;
  private LessonCorrectionResponse lessonCorrection;
  private Boolean isPremium;
  private Boolean canBeOpened;

}
