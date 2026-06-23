package fr.imcoding.edu365.persistence.repositories;

import fr.imcoding.edu365.persistence.entities.AccountActivation;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountActivationRepository extends JpaRepository<AccountActivation, Long> {

  Optional<AccountActivation> findByUuid(UUID activationCodeUuid);

  Optional<AccountActivation> findByActivationCode(String activationCode);

}
