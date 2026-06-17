package fr.imcoding.edu365.persistence.repositories;

import fr.imcoding.edu365.persistence.entities.InformationGiver;
import fr.imcoding.edu365.persistence.entities.InformationSeeker;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InformationSeekerRepository extends JpaRepository<InformationSeeker, Long> {
  Optional<InformationSeeker> findByUserEmail(String email);

}
