package fr.imcoding.edu365.business.mappers;

import fr.imcoding.edu365.dtos.SkillAreaDto;
import fr.imcoding.edu365.persistence.entities.SkillArea;
import fr.imcoding.edu365.persistence.repositories.SkillAreaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SkillAreaMapper {


  private final SkillAreaRepository skillAreaRepository;

  public SkillArea toSkillArea(SkillAreaDto skillAreaRequest) {
    return skillAreaRepository.findByUuid(skillAreaRequest.getSkillAreaUuid());
  }

  public SkillAreaDto toSkillAreaDto(SkillArea skillArea) {
    return new SkillAreaDto(skillArea.getUuid(), skillArea.getSkillAreaLabel(),
        skillArea.getSkillAreaCode());
  }
}
