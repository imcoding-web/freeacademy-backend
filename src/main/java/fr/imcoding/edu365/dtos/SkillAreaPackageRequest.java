package fr.imcoding.edu365.dtos;

import fr.imcoding.edu365.enumeration.PackageType;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 28/09/2023
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SkillAreaPackageRequest {

  private UUID uuid;
  private PackageType packageType;
  private Double packagePrice;


}
