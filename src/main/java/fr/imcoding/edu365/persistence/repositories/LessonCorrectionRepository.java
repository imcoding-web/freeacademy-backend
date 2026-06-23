package fr.imcoding.edu365.persistence.repositories;

import fr.imcoding.edu365.persistence.entities.LessonCorrection;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Rokaya
 * @Date 12/10/2023
 */
@Repository
public interface LessonCorrectionRepository extends JpaRepository<LessonCorrection,Long> {
  Optional<LessonCorrection> findByUuid(UUID uuid);
  Optional<LessonCorrection> findByCourseUuid(UUID lessonUuid);

}
