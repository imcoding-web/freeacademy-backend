package fr.imcoding.edu365.rest;

import fr.imcoding.edu365.business.services.TraceTeacherCourseService;
import fr.imcoding.edu365.dtos.MyProgressDto;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/trace-teacher-course")
@RequiredArgsConstructor
public class TraceTeacherCourseController {

  private final TraceTeacherCourseService traceTeacherCourseService;

  @PostMapping("/{courseId}")
  public void addTeacherCourseAccessTracability(@PathVariable("courseId") UUID courseId) {
    this.traceTeacherCourseService.addTeacherCourseAccessTracability(courseId);
  }

  @PutMapping("/{courseId}/complete")
  public void markAsCompleted(@PathVariable("courseId") UUID courseId) {
    this.traceTeacherCourseService.markAsCompleted(courseId);
  }

  @GetMapping("/my-progress")
  public MyProgressDto getMyProgress() {
    return this.traceTeacherCourseService.getMyProgress();
  }
}
