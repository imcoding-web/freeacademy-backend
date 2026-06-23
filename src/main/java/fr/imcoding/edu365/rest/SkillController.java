package fr.imcoding.edu365.rest;


import java.util.ArrayList;
import fr.imcoding.edu365.business.services.SkillService;
import fr.imcoding.edu365.business.services.UserService;
import fr.imcoding.edu365.dtos.SkillDto;
import fr.imcoding.edu365.persistence.entities.InformationSeeker;
import fr.imcoding.edu365.persistence.entities.User;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.imcoding.edu365.business.services.SkillService;
import fr.imcoding.edu365.business.services.UserService;
import fr.imcoding.edu365.dtos.SkillDto;
import fr.imcoding.edu365.persistence.entities.InformationSeeker;
import lombok.RequiredArgsConstructor;

/**
 * @author Rokaya
 * @Date 28/05/2022
 */

@RestController
@RequestMapping("/skill")
@CrossOrigin
@RequiredArgsConstructor
public class SkillController {

  private final SkillService skillService;
  private final UserService userService;


  //@PreAuthorize("hasAnyAuthority({'INFORMATION_GIVER'})")
  @GetMapping()
  public List<SkillDto> skills(@RequestParam(value = "skill-area-code") String skillAreaCode) {
    return skillService.getSkillsBySkillAreaCode(skillAreaCode);
  }

  @GetMapping(value = "filtred")
  public List<SkillDto> getFiltredSkills(
      @RequestParam(value = "skill-levels") String[] skillAreaLabel,
      @RequestParam(name = "skill_label", required = false)
          String skillLabel,
      @RequestParam(name = "page", required = false, defaultValue = "0")
          Integer page,
      @RequestParam(name = "offset", required = false)
          Integer offset) {
    return skillService.getFiltredSkills(skillAreaLabel,page, offset, skillLabel);
  }
  @GetMapping(value="user-skills")
  public List<SkillDto> skills() {
    InformationSeeker currentUser=(InformationSeeker)userService.getCurrentUser();
    if(currentUser.getCurrentLevel() == null) {
    	return new ArrayList<>();
    }
    return skillService.getSkillsBySkillAreaCode(currentUser.getCurrentLevel().getSkillAreaCode());
  }
}
