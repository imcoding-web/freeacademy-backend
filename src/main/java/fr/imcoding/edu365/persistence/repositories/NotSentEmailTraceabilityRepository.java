package fr.imcoding.edu365.persistence.repositories;

import fr.imcoding.edu365.enumeration.EmailContext;
import fr.imcoding.edu365.persistence.entities.NotSentEmailTraceability;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Rokaya
 * @Date 07/11/2022
 */
public interface NotSentEmailTraceabilityRepository extends JpaRepository<NotSentEmailTraceability,Long> {
 Optional<NotSentEmailTraceability> findByUuid(UUID uuid);
 Optional<NotSentEmailTraceability> findByEmailAndContext(String mail,EmailContext emailContext);
 Page<NotSentEmailTraceability> findByContext(EmailContext emailContext,Pageable pageable);


}
