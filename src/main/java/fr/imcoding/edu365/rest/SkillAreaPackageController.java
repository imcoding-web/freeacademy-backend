package fr.imcoding.edu365.rest;

import fr.imcoding.edu365.business.services.SkillAreaPackageService;
import fr.imcoding.edu365.dtos.SkillAreaPackageRequest;
import fr.imcoding.edu365.dtos.SkillAreaWithPackagesDTO;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Rokaya
 * @Date 28/09/2023
 */

@RestController
@RequestMapping("/package")
@CrossOrigin
@RequiredArgsConstructor
public class SkillAreaPackageController {
  private final SkillAreaPackageService skillAreaPackageService;


  @GetMapping
  public List<SkillAreaWithPackagesDTO> getAllSkillAreasWithPackages() {
    return skillAreaPackageService.getAllSkillAreasWithPackages();
  }

  @PostMapping()
  public void savePackage(@RequestParam(value = "skill-area-code") String skillAreaCode,@RequestBody
      SkillAreaPackageRequest skillAreaPackageRequest) {
    this.skillAreaPackageService.addSkillAreaPackage(skillAreaCode,skillAreaPackageRequest);

  }

  @GetMapping("/list")
  public List<SkillAreaPackageRequest> getAllPackages() {
    return skillAreaPackageService.getAllPackages();
  }

  @GetMapping("/by-skill-area")
  public List<SkillAreaPackageRequest> getAllPackagesBySkillAreaUuid(@RequestParam(value = "skill-area-code")
      String skillAreaCode) {
    return skillAreaPackageService.getAllPackagesBySkillLevel(skillAreaCode);
  }
}
