package fr.imcoding.edu365.sceduled;

import fr.imcoding.edu365.business.services.TeacherCourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Publishes planned teacher courses when their schedule is due.
 */
@RequiredArgsConstructor
@Slf4j
@Component
@EnableScheduling
public class PublishTeacherCourseTask {

  private final TeacherCourseService teacherCourseService;

  @Scheduled(cron = "${edu365.teacher-course.scheduler.automatic.publish.cron:0 */1 * * * *}")
  public void publishDueTeacherCourses() {
    log.info("***START AUTOMATIC TEACHER COURSE PUBLICATION***");
    teacherCourseService.publishDueScheduledCourses();
    log.info("***END AUTOMATIC TEACHER COURSE PUBLICATION***");
  }
}
