package fr.imcoding.edu365.persistence.repositories;

import fr.imcoding.edu365.enumeration.DaysOfWeek;
import fr.imcoding.edu365.persistence.entities.InformationGiver;
import fr.imcoding.edu365.persistence.entities.User;
import fr.imcoding.edu365.persistence.entities.UserAvailability;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author Rokaya
 * @Date 03/07/2022
 */
public interface UserAvailabilityRepository extends JpaRepository<UserAvailability,Long> {
  UserAvailability findByUuid(UUID uuid);
  void deleteByUuid(UUID  uuid);

  List<UserAvailability> findAllByUserAvailabilityExpertUuid(UUID userUuid);

  @Query(
      "SELECT uv FROM UserAvailability uv WHERE uv.userAvailabilityExpert = ?1 AND uv.userAvailabilityDay = ?2 AND "
          + "(uv.userAvailabilityEndDate = ?3 OR uv.userAvailabilityStartDate = ?4 OR (uv.userAvailabilityStartDate > ?3 AND uv.userAvailabilityEndDate < ?4) "
          + "OR (uv.userAvailabilityStartDate <= ?3 AND uv.userAvailabilityEndDate > ?3) OR (uv.userAvailabilityStartDate < ?4 AND uv.userAvailabilityEndDate >= ?4))")
  List<UserAvailability> findSimilarUserAvailability(
      InformationGiver user, DaysOfWeek day, Date startDate, Date endDate);



  @Query(
      "SELECT uv FROM UserAvailability uv WHERE uv.userAvailabilityExpert = ?1 AND uv.userAvailabilityDay = ?2")
  List<UserAvailability> findSimilarUserAvailabilityByDay(
      User user, DaysOfWeek day);
}
