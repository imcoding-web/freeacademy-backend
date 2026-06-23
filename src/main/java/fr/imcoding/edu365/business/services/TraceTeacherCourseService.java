package fr.imcoding.edu365.business.services;

import fr.imcoding.edu365.dtos.MyProgressDto;
import fr.imcoding.edu365.persistence.entities.TeacherCourse;
import fr.imcoding.edu365.persistence.entities.TraceTeacherCourse;
import fr.imcoding.edu365.persistence.entities.User;
import fr.imcoding.edu365.persistence.entities.UserCourseCompletion;
import fr.imcoding.edu365.persistence.repositories.TeacherCourseRepository;
import fr.imcoding.edu365.persistence.repositories.TraceTeacherCourseRepository;
import fr.imcoding.edu365.persistence.repositories.UserCourseCompletionRepository;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TraceTeacherCourseService {

  private final TraceTeacherCourseRepository traceTeacherCourseRepository;
  private final UserCourseCompletionRepository completionRepository;
  private final TeacherCourseRepository teacherCourseRepository;
  private final UserService userService;

  // Toujours insérer une nouvelle ligne — journal de visites
  @Async
  public void addTeacherCourseAccessTracability(TeacherCourse teacherCourse, User user) {
    TraceTeacherCourse trace = new TraceTeacherCourse();
    trace.setCourseTitle(teacherCourse.getTitle());
    trace.setTeacherCourse(teacherCourse);
    trace.setUser(user);
    trace.setUserFullName(user != null ? user.getFullName() : "Anonyme");
    trace.setUserEmail(user != null ? user.getUserEmail() : "Anonyme");
    trace.setUserPhoneNumber(user != null ? user.getUserPhoneNumber() : "Anonyme");
    traceTeacherCourseRepository.save(trace);
  }

  public void addTeacherCourseAccessTracability(UUID courseId) {
    TeacherCourse teacherCourse = teacherCourseRepository.findByUuid(courseId).orElse(null);
    if (teacherCourse == null) return;
    User user = null;
    try {
      user = userService.getCurrentUser();
    } catch (Exception ignored) {}
    addTeacherCourseAccessTracability(teacherCourse, user);
  }

  // Toggle : présence dans edu365_user_course_completion = terminé
  public void markAsCompleted(UUID courseId) {
    User user = userService.getCurrentUser();
    TeacherCourse teacherCourse = teacherCourseRepository.findByUuid(courseId).orElse(null);
    if (teacherCourse == null) return;

    java.util.Optional<UserCourseCompletion> existing =
        completionRepository.findByUserAndTeacherCourse(user, teacherCourse);
    if (existing.isPresent()) {
      completionRepository.delete(existing.get());
    } else {
      UserCourseCompletion completion = new UserCourseCompletion();
      completion.setUser(user);
      completion.setTeacherCourse(teacherCourse);
      completionRepository.save(completion);
    }
  }

  public MyProgressDto getMyProgress() {
    User user = userService.getCurrentUser();

    // Visités = cours distincts dans le journal de traces
    List<String> visited = traceTeacherCourseRepository.findByUser(user).stream()
        .map(t -> t.getTeacherCourse().getUuid().toString())
        .distinct()
        .collect(Collectors.toList());

    // Terminés = présents dans la table de complétion
    List<String> completed = completionRepository.findByUser(user).stream()
        .map(c -> c.getTeacherCourse().getUuid().toString())
        .collect(Collectors.toList());

    return new MyProgressDto(visited, completed);
  }
}
