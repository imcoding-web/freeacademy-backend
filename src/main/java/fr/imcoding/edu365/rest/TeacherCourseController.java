package fr.imcoding.edu365.rest;

import fr.imcoding.edu365.business.services.LessonCorrectionService;
import fr.imcoding.edu365.business.services.TeacherCourseCorrectionService;
import fr.imcoding.edu365.business.services.TeacherCourseService;
import fr.imcoding.edu365.dtos.PageDto;
import fr.imcoding.edu365.dtos.TeacherCourseDetails;
import fr.imcoding.edu365.dtos.TeacherCourseRequest;
import fr.imcoding.edu365.dtos.TeacherCourseResponse;
import fr.imcoding.edu365.enumeration.CourseType;
import fr.imcoding.edu365.enumeration.Quarter;
import fr.imcoding.edu365.persistence.entities.TeacherCourse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Rokaya
 * @Date 10/09/2023
 */
@RestController
@CrossOrigin
@RequestMapping("/teacher-course")
@RequiredArgsConstructor
public class TeacherCourseController {

  private final TeacherCourseService teacherCourseService;
  private final LessonCorrectionService lessonCorrectionService;
  private final TeacherCourseCorrectionService teacherCourseCorrectionService;

  @PostMapping("/add")
  public void saveOwner(@ModelAttribute TeacherCourseRequest teacherCourseRequest) {
      this.teacherCourseService.saveTeacherCourse(teacherCourseRequest);

  }

  @GetMapping
  public List<TeacherCourseResponse> getTeacherCourses(){
    return teacherCourseService.getTeacherCourse();
  }

  @DeleteMapping("/{courseUuid}")
  public void deleteProject(
      @PathVariable UUID courseUuid) {
    teacherCourseService.deleteTeacherCourse(courseUuid);
  }

  @GetMapping("/{courseUuid}")
  public TeacherCourseResponse getTeacherCourse(@PathVariable UUID courseUuid){
    return teacherCourseService.getTeacherCourseDetails(courseUuid);
  }

  @PatchMapping(value="/update-course",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public TeacherCourse updateTeacherCourse(@ModelAttribute TeacherCourseRequest teacherCourseRequest) {
    return this.teacherCourseService.updateTeacherCourse(teacherCourseRequest);
  }

  @PatchMapping(value="/publication-schedule/{courseUuid}", produces = MediaType.APPLICATION_JSON_VALUE)
  public TeacherCourseResponse updateTeacherCoursePublicationSchedule(
      @PathVariable UUID courseUuid,
      @RequestParam(name = "plannedPublicationDateTime", required = false)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
      LocalDateTime plannedPublicationDateTime) {
    return this.teacherCourseService.updateTeacherCoursePublicationSchedule(courseUuid, plannedPublicationDateTime);
  }

  @PatchMapping(value="/update-visibility/{courseUuid}")
  public void updateCourseVisibility(@PathVariable("courseUuid") UUID courseId, @RequestParam("shouldBeDisplayed") Boolean shouldBeDisplayed) {
     this.teacherCourseService.updateCourseVisibility(courseId, shouldBeDisplayed);
  }

  @GetMapping("/last-six-course")
  public List<TeacherCourseDetails> getLast6Course(){
    return teacherCourseService.getLast6Course();
  }

  @GetMapping(value = "/filtred")
  public PageDto<TeacherCourseDetails> getLessonsPaginated(
      @RequestParam(name = "page") Integer page,
      @RequestParam(name = "offset") Integer offset,
      @RequestParam(name = "skill", required = false)
          String skill,
      @RequestParam(name = "type", required = false)
          CourseType type,
      @RequestParam(name = "quarter", required = false)
          Quarter quarter) {

    return teacherCourseService.filterCourses(
        page, offset,skill,type,quarter);
  }

  @GetMapping(value = "/filtred-for-admin")
  public PageDto<TeacherCourseResponse> filterCoursesForAdmin(
      @RequestParam(name = "skillAreaCode", required = false)
      String skillAreaCode,
      @RequestParam(name = "sectionCode", required = false)
      String sectionCode,
      @RequestParam(name = "skillLabel", required = false)
      String skillLabel,
      @RequestParam(name = "quarter", required = false)
      Quarter quarter,
      @RequestParam(name = "type", required = false)
      CourseType type) {

    return teacherCourseService.filterCoursesForAdmin(
        skillAreaCode,sectionCode, skillLabel,quarter, type);
  }


	@GetMapping("/details/{uuid}")
	public ResponseEntity<TeacherCourseResponse> getLessonDetailsById(@PathVariable(name = "uuid") UUID lessonUuid) {
		TeacherCourseResponse teacherCourse = this.teacherCourseService.getTeacherCourse(lessonUuid);
		if (teacherCourse == null)
			return ResponseEntity.notFound().build();
		return ResponseEntity.ok(teacherCourse);
	}

  @GetMapping("/correction-details/{uuid}")
  public TeacherCourseResponse getLessonCorrectionDetails(
      @PathVariable(name = "uuid") UUID lessonUuid) {
    return this.teacherCourseCorrectionService.getLessonCorrectionDetail(lessonUuid);
  }

  @GetMapping(value = "/similar/{uuid}")
  public List<TeacherCourseDetails> getSimilarLessons(
      @PathVariable(name = "uuid") UUID lessonUuid) {
    return teacherCourseService.getSimilarLessons(
        lessonUuid);
  }
}
