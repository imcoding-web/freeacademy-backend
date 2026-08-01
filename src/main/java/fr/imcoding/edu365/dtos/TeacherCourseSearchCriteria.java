package fr.imcoding.edu365.dtos;

import fr.imcoding.edu365.enumeration.CoursePublicationStatus;
import fr.imcoding.edu365.enumeration.CourseType;
import fr.imcoding.edu365.enumeration.Quarter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 19/09/2023
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherCourseSearchCriteria {
  private String skillArea;
  private String skillAreaSection;
  private String skill;
  private CourseType type;
  private Quarter quarter;
  private Boolean shouldBeDisplayed;
  private CoursePublicationStatus publicationStatus;

}
