package fr.imcoding.edu365.client.dtos.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import fr.imcoding.edu365.dtos.ExpertDetails;
import fr.imcoding.edu365.dtos.MediaDetails;
import java.util.Date;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 25/12/2022
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequestResponse {

  private UUID transfertUuid;
  private UUID expertUuid;

  private String expertFullname;
 /* private MediaDetails expertProfilePicture;
  private String rib;*/
  private double accumulatedBalance;
  private double unpaidAccumulatedBalance;
  private double amount;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Africa/Tunis")
  private Date transferCreatedDate;


}
