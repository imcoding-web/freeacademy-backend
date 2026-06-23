package fr.imcoding.edu365.dtos;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 26/09/2022
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DegreeDto {
  private UUID degreeUuid;
  private String degreeLabel;
  private String degreeCode;
}
