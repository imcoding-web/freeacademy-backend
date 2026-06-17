package fr.imcoding.edu365.dtos;

import fr.imcoding.edu365.persistence.entities.Speciality;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 09/06/2022
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PositionDto {

  private UUID positionUuid;
  private String positionLabel;
  private String positionCode;

}
