package fr.imcoding.edu365.business.mappers;

import fr.imcoding.edu365.dtos.SkillDto;
import fr.imcoding.edu365.persistence.entities.Skill;
import fr.imcoding.edu365.persistence.repositories.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SkillMapper {

  private final SkillRepository skillRepository;
  private final SkillAreaMapper skillAreaMapper;


  public Skill toSkill(SkillDto skillRequest) {
    return skillRepository.findByUuid(skillRequest.getSkillUuid());
  }

  public SkillDto toSkillDto(Skill skill) {
	  if(skill == null) return null;
    return SkillDto.builder().skillUuid(skill.getUuid()).skillLabel(skill.getSkillLabel()).skillCode(skill.getSkillCode()).build();
  }

  public SkillDto toProjectSkillDto(Skill skill) {
    return SkillDto.builder().skillUuid(skill.getUuid()).skillLabel(skill.getSkillLabel()).skillCode(skill.getSkillCode()).skillArea(skillAreaMapper.toSkillAreaDto(skill.getSkillArea())).build();
  }

}
