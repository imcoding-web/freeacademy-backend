package fr.imcoding.edu365.dtos;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkillAreaDto {

  private UUID skillAreaUuid;
  private String skillAreaLabel;
  private String skillAreaCode;
}
