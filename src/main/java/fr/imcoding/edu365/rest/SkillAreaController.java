package fr.imcoding.edu365.rest;

import fr.imcoding.edu365.business.services.SkillAreaService;
import fr.imcoding.edu365.dtos.PageDto;
import fr.imcoding.edu365.dtos.SkillAreaDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Rokaya
 * @Date 28/05/2022
 */
@RestController
@RequestMapping("/skill-area")
@CrossOrigin
@RequiredArgsConstructor
public class SkillAreaController {

  private final SkillAreaService skillAreaService;
  //@PreAuthorize("hasAnyAuthority({'INFORMATION_GIVER'})")
  @GetMapping()
  public List<SkillAreaDto> getAllSkillAreas() {
    return this.skillAreaService.getAllSkillArea();
  }

  @GetMapping(value = "filtred")
  public PageDto<SkillAreaDto> getAllSkillLevels(
      @RequestParam(name = "skill-area-label", required = false)
          String skillAreaLabel,
      @RequestParam(name = "page", required = false, defaultValue = "0")
          Integer page,
      @RequestParam(name = "offset", required = false)
          Integer offset) {
    return skillAreaService.getAllSkillArea(page, offset, skillAreaLabel);
  }
}
