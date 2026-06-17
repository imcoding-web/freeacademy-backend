package fr.imcoding.edu365.persistence.repositories;

import fr.imcoding.edu365.persistence.entities.SkillArea;
import fr.imcoding.edu365.persistence.entities.SkillAreaSection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SkillAreaSectionRepository extends JpaRepository<SkillAreaSection, Long> {

  SkillAreaSection findByUuid(UUID uuid);
  SkillAreaSection findByCode (String code);
}
