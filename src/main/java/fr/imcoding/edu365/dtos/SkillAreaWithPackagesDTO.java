package fr.imcoding.edu365.dtos;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 28/09/2023
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkillAreaWithPackagesDTO {
  private SkillAreaDto skillArea;
  private List<SkillAreaPackageRequest> skillAreaPackages;
}
