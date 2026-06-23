package fr.imcoding.edu365.business.services;

import com.google.common.base.Strings;
import fr.imcoding.edu365.business.mappers.SkillMapper;
import fr.imcoding.edu365.dtos.SkillDto;
import fr.imcoding.edu365.persistence.entities.Skill;
import fr.imcoding.edu365.persistence.repositories.SkillRepository;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
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
public class SkillService {

  private final SkillRepository skillRepository;
  private final SkillMapper skillMapper;


  public List<SkillDto> getSkillsBySkillAreaCode(String skillAreaCode) {
    return this.skillRepository.findBySkillAreaSkillAreaCodeOrderBySkillLabel(skillAreaCode).stream()
        .map(skill -> skillMapper.toSkillDto(skill)).collect(Collectors
            .toList());

  }

  public Skill getSkillsBySkillCode(String skillAreaCode) {
    return this.skillRepository.findBySkillCode(skillAreaCode);

  }
//
  public List<SkillDto> getFiltredSkills(String[] skillAreaLabel,
      Integer page, Integer offset, String skillLabel) {
    log.info("Get all filtred Skill entities by label {}", skillLabel);
    Pageable pageable = null;

    if (offset == null) {
      pageable =
          PageRequest.of(page, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "skillLabel"));
    } else {
      pageable = PageRequest.of(page, offset, Sort.by(Sort.Direction.ASC, "skillLabel"));
    }
if(skillAreaLabel.length>0){
  if (Strings.isNullOrEmpty(skillLabel)) {
    return skillRepository.findBySkillAreaSkillAreaLabelIn(skillAreaLabel, pageable).stream()
        .filter(distinctByKey(Skill::getSkillLabel))
        .collect(Collectors.toList()).stream()
        .map(s -> skillMapper.toSkillDto(s))
        .collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());
  }else{
    return skillRepository.findBySkillAreaSkillAreaLabelInAndSkillLabelStartingWith(skillAreaLabel,skillLabel, pageable).stream()
        .filter(distinctByKey(Skill::getSkillLabel))
        .collect(Collectors.toList()).stream().map(s -> skillMapper.toSkillDto(s)).collect(Collectors.toList()).stream().distinct()
        .collect(Collectors.toList());
  }
}else {
  if (Strings.isNullOrEmpty(skillLabel)) {

    return skillRepository.findAll(pageable).stream()
        .filter(distinctByKey(Skill::getSkillLabel))
        .collect(Collectors.toList()).stream().map(s -> skillMapper.toSkillDto(s))
        .collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());
  } else {

    return skillRepository.findAllBySkillLabelStartingWith(skillLabel, pageable).stream()
        .filter(distinctByKey(Skill::getSkillLabel))
        .collect(Collectors.toList()).stream()
        .map(s -> skillMapper.toSkillDto(s)).collect(Collectors.toList()).stream().distinct()
        .collect(Collectors.toList());
  }
}
  }


  public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
    Map<Object, Boolean> seen = new ConcurrentHashMap<>();
    return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
  }
  
public Skill findByCode(String skillCode) {
	return this.skillRepository.findBySkillCode(skillCode);
}
}
