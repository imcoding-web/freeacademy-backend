package fr.imcoding.edu365.client.dtos.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.OptBoolean;
import java.util.Date;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 31/08/2022
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcceptOfferRequest {
  private UUID offerUuid;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private Date videoconferenceStartDate;
  @JsonFormat(
      shape = JsonFormat.Shape.STRING,
      pattern = "HH:mm",
      lenient = OptBoolean.FALSE)
  private Date videoconferenceStartTime;
  private String wordForExpert;
}
