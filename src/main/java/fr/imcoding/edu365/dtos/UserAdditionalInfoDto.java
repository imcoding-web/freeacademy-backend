package fr.imcoding.edu365.dtos;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 16/06/2022
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAdditionalInfoDto {
  private UUID uuid;
  private String lastGraduationYear;
  private DegreeDto currentGraduation;
  private DegreeDto lastGraduation;
  private String customSpeciality;

}
