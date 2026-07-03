package fr.imcoding.edu365.persistence.repositories;

import fr.imcoding.edu365.enumeration.ValidationStatus;
import fr.imcoding.edu365.persistence.entities.VerifiedExpert;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author Rokaya
 * @Date 17/07/2022
 */
public interface VerifiedExpertRepository extends JpaRepository<VerifiedExpert, Long> {
    List<VerifiedExpert> findByUserUuid(UUID userUuid);
    List<VerifiedExpert> findByStatus(ValidationStatus status);
    VerifiedExpert findByUuid(UUID uuid);
}
