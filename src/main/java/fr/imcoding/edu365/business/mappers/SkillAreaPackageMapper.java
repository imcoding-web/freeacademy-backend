package fr.imcoding.edu365.business.mappers;

import fr.imcoding.edu365.dtos.SkillAreaPackageRequest;
import fr.imcoding.edu365.dtos.SkillAreaWithPackagesDTO;
import fr.imcoding.edu365.persistence.entities.SkillAreaPackage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Rokaya
 * @Date 28/09/2023
 */
@Component
@RequiredArgsConstructor
public class SkillAreaPackageMapper {

  public SkillAreaPackageRequest toSkillAreaPackageRequest(SkillAreaPackage skillAreaPackage){
    return SkillAreaPackageRequest.builder().uuid(skillAreaPackage.getUuid()).packagePrice(skillAreaPackage.getPackagePrice()).packageType(skillAreaPackage.getPackageType()).build();
  }

}
