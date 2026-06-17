package fr.imcoding.edu365.persistence.repositories;

import fr.imcoding.edu365.persistence.entities.SkillArea;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillAreaRepository extends JpaRepository<SkillArea, Long> {

  SkillArea findByUuid(UUID uuid);

  SkillArea findBySkillAreaCode(String skillAreaCode);

  Page<SkillArea>  findBySkillAreaLabelStartingWith(String skillAreaLabel, Pageable pageable);
  List<SkillArea> findAllByShouldBeDisplayedTrue();

}
