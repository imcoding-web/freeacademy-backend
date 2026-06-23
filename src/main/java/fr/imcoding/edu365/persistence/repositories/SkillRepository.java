package fr.imcoding.edu365.persistence.repositories;

import fr.imcoding.edu365.persistence.entities.Skill;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {

  Skill findByUuid(UUID uuid);

  Skill findBySkillCode(String skillCode);

  List<Skill> findBySkillAreaSkillAreaCodeOrderBySkillLabel(String skillAreaCode);

  List<Skill> findBySkillAreaSkillAreaLabel(String skillAreaLabel, Pageable pageable);

  List<Skill> findBySkillAreaSkillAreaLabelIn(String[] skillAreaLabel, Pageable pageable);
  List<Skill> findBySkillAreaSkillAreaLabelInAndSkillLabelStartingWith(String[] skillAreaLabel,String skillLabel, Pageable pageable);



  Page<Skill> findAllBySkillLabelStartingWith(
      String skillLabel, Pageable pageable);

}
