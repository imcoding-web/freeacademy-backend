package fr.imcoding.edu365.persistence.repositories;

import fr.imcoding.edu365.persistence.entities.TeacherCourse;
import fr.imcoding.edu365.persistence.entities.User;
import fr.imcoding.edu365.persistence.entities.UserCourseCompletion;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCourseCompletionRepository extends JpaRepository<UserCourseCompletion, Long> {

  Optional<UserCourseCompletion> findByUserAndTeacherCourse(User user, TeacherCourse teacherCourse);

  List<UserCourseCompletion> findByUser(User user);
}
