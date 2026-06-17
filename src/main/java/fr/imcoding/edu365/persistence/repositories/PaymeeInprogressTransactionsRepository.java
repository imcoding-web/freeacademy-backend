package fr.imcoding.edu365.persistence.repositories;

import fr.imcoding.edu365.persistence.entities.PaymeeInProgressTransaction;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Rokaya
 * @Date 03/12/2022
 */
public interface PaymeeInprogressTransactionsRepository extends JpaRepository<PaymeeInProgressTransaction,Long> {
  Optional<PaymeeInProgressTransaction> findByToken(String token);
}
