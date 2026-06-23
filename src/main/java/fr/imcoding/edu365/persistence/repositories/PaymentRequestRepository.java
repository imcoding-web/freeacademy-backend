package fr.imcoding.edu365.persistence.repositories;

import fr.imcoding.edu365.enumeration.PaymentRequestStatus;
import fr.imcoding.edu365.persistence.entities.PaymentRequest;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Rokaya
 * @Date 25/12/2022
 */
public interface PaymentRequestRepository extends JpaRepository<PaymentRequest,Long> {
  List<PaymentRequest> findByExpertUuid(UUID expertUuid);
  PaymentRequest findByUuid(UUID uuid);

  List<PaymentRequest> findByPaymentRequestStatus(PaymentRequestStatus paymentRequestStatus);


}
