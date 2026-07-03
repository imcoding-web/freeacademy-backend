package fr.imcoding.edu365.persistence.repositories;

import fr.imcoding.edu365.persistence.entities.UserBankData;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Rokaya
 * @Date 15/07/2022
 */
public interface UserBankDataRepository extends JpaRepository<UserBankData,Long> {

  UserBankData findByUserUuid(UUID userUuid);
  UserBankData findByUuid(UUID uuid);
}
