package fr.imcoding.edu365.persistence.repositories;

import fr.imcoding.edu365.enumeration.PrestationStatus;
import fr.imcoding.edu365.persistence.entities.Prestation;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Rokaya
 * @Date 24/10/2022
 */
public interface PrestationRepository extends JpaRepository<Prestation,Long> {

  List<Prestation> findByOfferOfferGiverUuid(UUID uuid);
  List<Prestation> findByOfferAnnouncementAnnouncementPublisherUuid (UUID uuid);
  List<Prestation> findByPrestationStatus (PrestationStatus prestationStatus);


  Optional<Prestation> findByUuid(UUID uuid);

}
