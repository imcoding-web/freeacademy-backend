package fr.imcoding.edu365.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import fr.imcoding.edu365.enumeration.ValidationStatus;
import fr.imcoding.edu365.utils.Constants;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 18/07/2022
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidationHistoryDto {
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = Constants.DEFAULT_TIMEZONE)
  private Date validationDate;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = Constants.DEFAULT_TIMEZONE)
  private Date validationRequestDate;
  private ValidationStatus validationStatus;
  private String refusalReason;

}
