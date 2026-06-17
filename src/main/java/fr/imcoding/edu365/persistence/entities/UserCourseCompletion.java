package fr.imcoding.edu365.persistence.entities;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
  name = "edu365_user_course_completion",
  uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "teacher_course_id"})
)
@Data
@NoArgsConstructor
public class UserCourseCompletion extends BaseEntity {

  @ManyToOne
  private User user;

  @ManyToOne
  private TeacherCourse teacherCourse;
}
