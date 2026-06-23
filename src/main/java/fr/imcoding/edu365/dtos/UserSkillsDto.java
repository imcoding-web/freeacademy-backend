package fr.imcoding.edu365.dtos;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 01/06/2022
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSkillsDto {

  private String skillAreaCode;
  private String skillAreaLabel;
  private List<SkillDto> skills;

}
