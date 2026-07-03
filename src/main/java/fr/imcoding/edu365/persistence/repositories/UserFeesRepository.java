package fr.imcoding.edu365.persistence.repositories;

import fr.imcoding.edu365.enumeration.FeesType;
import fr.imcoding.edu365.enumeration.PaymentStatus;
import fr.imcoding.edu365.persistence.entities.UserFees;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Rokaya
 * @Date 27/01/2023
 */
public interface UserFeesRepository extends JpaRepository<UserFees,Long> {
  Optional<UserFees> findByUuid(UUID paymentUuid);
  Optional<UserFees> findByUserUuid(UUID paymentUuid);
  UserFees findByUserUuidAndFeesType(UUID userUuid,FeesType feesType);
  UserFees findByUserUuidAndFeesTypeAndPaymentStatus(UUID userUuid,FeesType feesType,PaymentStatus paymentStatus);
  UserFees findByUniqueIdentifier(String id);
  List<UserFees> findByPaymentStatus(PaymentStatus paymentStatus);

}
