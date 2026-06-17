package fr.imcoding.edu365.business.mappers;

import fr.imcoding.edu365.dtos.TeacherCourseDetails;
import fr.imcoding.edu365.persistence.entities.LessonCorrection;
import fr.imcoding.edu365.persistence.entities.TeacherCourse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Rokaya
 * @Date 13/10/2023
 */
@Component
@RequiredArgsConstructor
public class TeacherCourceCorrectionMapper {
  private final LessonCorrectionMapper lessonCorrectionMapper;
  private final TeacherCourseMapper teacherCourseMapper;


  public TeacherCourseDetails getTeacherCourseDetailsByUuid(TeacherCourse teacherCourse,LessonCorrection lessonCorrection) {
    return teacherCourseMapper
        .toLessonCorrectionDetails(teacherCourse,lessonCorrectionMapper.toLessonCorrectionResponse(lessonCorrection));
  }
  public TeacherCourseDetails toTeacherCourseDetails(TeacherCourse teacherCourse,LessonCorrection lessonCorrection) {
    return teacherCourseMapper
        .toLessonDetails(teacherCourse,lessonCorrectionMapper.toLessonCorrectionResponse(lessonCorrection));
  }
}
