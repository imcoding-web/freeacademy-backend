package fr.imcoding.edu365.client.dtos.response;

import fr.imcoding.edu365.client.dtos.request.PaymentRequestDto;
import fr.imcoding.edu365.dtos.MediaDetails;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 25/12/2022
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpertPaymentResponse {
  private UUID expertUuid;
  private String expertFullname;
  private MediaDetails expertProfilePicture;
  private String rib;
  private double accumulatedBalance;
  private double unpaidAccumulatedBalance;
  private List<PaymentRequestDto> amountTransfertsHistory;

}
