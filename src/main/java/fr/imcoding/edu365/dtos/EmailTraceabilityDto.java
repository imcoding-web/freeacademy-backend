package fr.imcoding.edu365.dtos;

import fr.imcoding.edu365.enumeration.EmailContext;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 07/11/2022
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailTraceabilityDto {
  private String email;
  private EmailContext context;
  private String subject;
  private String templateName;
  private String extraInformation;

}
