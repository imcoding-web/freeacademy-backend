package fr.imcoding.edu365.persistence.repositories;

import fr.imcoding.edu365.enumeration.CoursePublicationStatus;
import fr.imcoding.edu365.enumeration.CourseType;
import fr.imcoding.edu365.enumeration.Quarter;
import fr.imcoding.edu365.persistence.entities.TeacherCourse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

/**
 * @author Rokaya
 * @Date 07/09/2023
 */
@Repository
public interface TeacherCourseRepository extends JpaRepository<TeacherCourse,Long>,JpaSpecificationExecutor<TeacherCourse> {
  Optional<TeacherCourse> findByUuid(UUID uuid);

  List<TeacherCourse> findTop6ByOrderByCreatedAtDesc();
  List<TeacherCourse> findTop6BySkillAreaSkillAreaCodeAndSkillSkillCodeOrderByCreatedAtDesc(String SkillAreaCode,String SkillCode);
  @Query("SELECT tc FROM TeacherCourse tc WHERE TRIM(tc.title) = TRIM(:title)")
  List<TeacherCourse> findByTitle(String title);

  //used for migrations
  List<TeacherCourse> findBySkillAreaIdAndSkillAreaSectionIdAndSkillId(Long skillAreaId, Long sectionId, Long skillId);
  List<TeacherCourse> findBySkillAreaIdAndSkillAreaSectionIdAndSkillIdAndTitle(Long skillAreaId, Long sectionId, Long skillId, String title);

  List<TeacherCourse> findByPublicationStatusAndPlannedPublicationDateTimeLessThanEqual(CoursePublicationStatus publicationStatus, LocalDateTime plannedPublicationDateTime);
  List<TeacherCourse> findByPublicationStatus(CoursePublicationStatus publicationStatus);
}
