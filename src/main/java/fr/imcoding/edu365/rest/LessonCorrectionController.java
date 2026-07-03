package fr.imcoding.edu365.rest;

import fr.imcoding.edu365.business.services.LessonCorrectionService;
import fr.imcoding.edu365.dtos.LessonCorrectionRequest;
import fr.imcoding.edu365.dtos.LessonCorrectionResponse;
import fr.imcoding.edu365.dtos.TeacherCourseDetails;
import fr.imcoding.edu365.persistence.entities.LessonCorrection;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Rokaya
 * @Date 12/10/2023
 */
@RestController
@CrossOrigin
@RequestMapping("/lesson-correction")
@RequiredArgsConstructor
public class LessonCorrectionController {

  private final LessonCorrectionService lessonCorrectionService;

  @PostMapping("/add")
  public void saveLessonCorrection(@ModelAttribute LessonCorrectionRequest lessonCorrectionRequest) {
    this.lessonCorrectionService.saveLessonCorrection(lessonCorrectionRequest);

  }



  @PatchMapping(value="/update-correction",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public LessonCorrection updateLessonCorrection(@ModelAttribute LessonCorrectionRequest lessonCorrectionRequest) {
    return this.lessonCorrectionService.updateLessonCorrection(lessonCorrectionRequest);
  }

}
