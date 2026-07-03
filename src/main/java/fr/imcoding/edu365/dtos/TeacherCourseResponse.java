package fr.imcoding.edu365.dtos;

import fr.imcoding.edu365.enumeration.CourseType;
import fr.imcoding.edu365.enumeration.Quarter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 07/09/2023
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(Include.NON_NULL)
public class TeacherCourseResponse {
  private UUID courseUuid;
  private String creatorEmail;
  private String title;
  private String description ;
  private CourseType type;
  private Quarter quarter;
  private List<MediaDetails> medias = new ArrayList<>();
  private SkillAreaDto skillArea;
  private SkillAreaSectionDto skillAreaSection;
  private SkillDto skill;
  private Boolean isPremium;
  private Boolean shouldBeDisplayed;
  private LessonCorrectionResponse lessonCorrection;
  //ajouter pour afficger les detailsd e la correction
  private CourseCreatorDto courseCreator;


}
