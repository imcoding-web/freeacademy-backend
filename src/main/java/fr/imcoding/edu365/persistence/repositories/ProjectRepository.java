package fr.imcoding.edu365.persistence.repositories;

import fr.imcoding.edu365.persistence.entities.InformationGiver;
import fr.imcoding.edu365.persistence.entities.Project;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Rokaya
 * @Date 04/06/2022
 */
public interface ProjectRepository extends JpaRepository<Project,Long> {
  Optional<Project> findByUuid(UUID uuid);

  List<Project> getByProjectOwnerUuid(UUID userUuid);
}
