package fr.imcoding.edu365.business.services;

import com.google.common.base.Strings;
import fr.imcoding.edu365.business.mappers.SkillAreaMapper;
import fr.imcoding.edu365.dtos.PageDto;
import fr.imcoding.edu365.dtos.SkillAreaDto;
import fr.imcoding.edu365.persistence.entities.SkillArea;
import fr.imcoding.edu365.persistence.repositories.SkillAreaRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * @author Rokaya
 * @Date 28/05/2022
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SkillAreaService {

  private final SkillAreaRepository skillAreaRepository;
  private final SkillAreaMapper skillAreaMapper;

  public List<SkillAreaDto> getAllSkillArea() {
    return this.skillAreaRepository.findAll().stream()
        .map(skillArea -> skillAreaMapper.toSkillAreaDto(skillArea)).collect(Collectors
            .toList());

  }

  public PageDto<SkillAreaDto> getAllSkillArea(
      Integer page, Integer offset, String skillLevelLabel) {
    log.info("Get All paginated SkillArea");
    Page<SkillArea> skillLevels = null;
    Pageable pageable = null;
    if (offset == null) {
      pageable =
          PageRequest.of(page, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "skillAreaLabel"));
    } else {
      pageable = PageRequest.of(page, offset, Sort.by(Sort.Direction.ASC, "skillAreaLabel"));
    }
    if (Strings.isNullOrEmpty(skillLevelLabel)) {
      skillLevels = skillAreaRepository.findAll(pageable);
    } else {
      skillLevels =
          skillAreaRepository.findBySkillAreaLabelStartingWith(skillLevelLabel, pageable);
    }

    return new PageDto<>(
        skillLevels
            .get()
            .map(skillLevel -> skillAreaMapper.toSkillAreaDto(skillLevel))
            .collect(Collectors.toList()),
        skillLevels.getTotalElements());
  }
  
  public SkillArea findByCode(String skillAreaCode) {
	  return this.skillAreaRepository.findBySkillAreaCode(skillAreaCode);
  }

}
