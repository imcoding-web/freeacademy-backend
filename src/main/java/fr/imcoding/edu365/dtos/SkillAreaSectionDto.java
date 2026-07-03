package fr.imcoding.edu365.dtos;

import fr.imcoding.edu365.persistence.entities.SkillAreaSection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkillAreaSectionDto {

  private UUID uuid;
  private String label;
  private String code;


}
