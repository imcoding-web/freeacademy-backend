package fr.imcoding.edu365.persistence.repositories;

import fr.imcoding.edu365.persistence.entities.TraceTeacherCourse;
import fr.imcoding.edu365.persistence.entities.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Rokaya
 * @Date 07/09/2023
 */
@Repository
public interface TraceTeacherCourseRepository extends JpaRepository<TraceTeacherCourse, Long> {

  List<TraceTeacherCourse> findByUser(User user);
}
