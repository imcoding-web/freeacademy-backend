package fr.imcoding.edu365.persistence.repositories;

import fr.imcoding.edu365.persistence.entities.PrestationMeeting;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Rokaya
 * @Date 08/01/2023
 */
public interface PrestationMeetingRepository extends JpaRepository<PrestationMeeting,Long> {

Optional<PrestationMeeting> findByPrestationUuid(UUID uuid);
  Optional<PrestationMeeting> findByPrestationOfferUuid(UUID uuid);

}
