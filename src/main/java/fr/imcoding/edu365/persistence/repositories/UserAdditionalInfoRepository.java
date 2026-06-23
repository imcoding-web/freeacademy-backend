package fr.imcoding.edu365.persistence.repositories;

import fr.imcoding.edu365.persistence.entities.UserAdditionalInfo;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Rokaya
 * @Date 16/06/2022
 */
public interface UserAdditionalInfoRepository extends JpaRepository<UserAdditionalInfo,Long> {

  UserAdditionalInfo findByUserUuid(UUID userUuid);
  UserAdditionalInfo findByUuid(UUID uuid);



}
