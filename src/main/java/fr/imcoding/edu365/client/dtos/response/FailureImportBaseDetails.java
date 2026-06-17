package fr.imcoding.edu365.client.dtos.response;

import fr.imcoding.edu365.enumeration.FailureImportCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 12/08/2022
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FailureImportBaseDetails {
  private String cellValue;
  private int rowIndex;
  private FailureImportCode failureImportCode;

}
