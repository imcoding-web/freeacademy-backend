package fr.imcoding.edu365.client.dtos.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 12/08/2022
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImportResponse {
  private int successLineNumber;
  private int emptyLineNumber;
  private int failureLineNumber;
  private List<FailureImportBaseDetails> failureImportBaseDetailsList;

}
