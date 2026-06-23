package fr.imcoding.edu365.business.mappers;

import fr.imcoding.edu365.dtos.SkillAreaDto;
import fr.imcoding.edu365.dtos.SkillAreaSectionDto;
import fr.imcoding.edu365.persistence.entities.SkillArea;
import fr.imcoding.edu365.persistence.entities.SkillAreaSection;
import fr.imcoding.edu365.persistence.repositories.SkillAreaRepository;
import fr.imcoding.edu365.persistence.repositories.SkillAreaSectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SkillAreaSectionMapper {


  private final SkillAreaSectionRepository skillAreaSectionRepository;

  public SkillAreaSection toSkillAreaSection(SkillAreaSectionDto skillAreaSectionRequest) {
    if(skillAreaSectionRequest == null) return null;
    return skillAreaSectionRepository.findByUuid(skillAreaSectionRequest.getUuid());
  }

  public SkillAreaSectionDto toSkillAreaSectionDto(SkillAreaSection section) {
    if(section == null) return null;
    return new SkillAreaSectionDto(section.getUuid(), section.getLabel(), section.getCode());
  }
}
