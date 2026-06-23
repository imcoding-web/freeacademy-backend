package fr.imcoding.edu365.persistence.repositories;

import fr.imcoding.edu365.enumeration.PaymentStatus;
import fr.imcoding.edu365.enumeration.PaymentType;
import fr.imcoding.edu365.persistence.entities.InformationGiver;
import fr.imcoding.edu365.persistence.entities.Payment;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author Rokaya
 * @Date 05/10/2022
 */
public interface PaymentRepository extends JpaRepository<Payment,Long>,JpaSpecificationExecutor<Payment> {

  List<Payment> findByOfferAnnouncementAnnouncementPublisherUuid(UUID expertUuid);
  List<Payment> findByOfferOfferGiverUuidAndPaymentStatus(UUID expertUuid,PaymentStatus paymentStatus);
  List<Payment> findByPaymentStatus(PaymentStatus paymentStatus);
  List<Payment> findByPaymentType(PaymentStatus paymentStatus);
  Optional <Payment> findByUuid(UUID payentUuid);



}
