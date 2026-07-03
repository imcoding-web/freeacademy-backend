package fr.imcoding.edu365.persistence.repositories;

import fr.imcoding.edu365.persistence.entities.SobflousInprogressTransaction;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Rokaya
 * @Date 03/12/2022
 */
public interface SobflousInprogressTransactionsRepository extends JpaRepository<SobflousInprogressTransaction,Long> {
  Optional<SobflousInprogressTransaction> findByOfferUuid(UUID offerUuid);
  Optional<SobflousInprogressTransaction> findByTransmid(String transmId);

}
