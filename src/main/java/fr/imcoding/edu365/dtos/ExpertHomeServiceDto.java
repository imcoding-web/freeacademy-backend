package fr.imcoding.edu365.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 12/02/2023
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExpertHomeServiceDto {
  private boolean homeServices;
  private String homeServicesDescription;

}
