package fr.imcoding.edu365.client.dtos.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import fr.imcoding.edu365.dtos.MediaDetails;
import fr.imcoding.edu365.dtos.OfferResponseDto;
import fr.imcoding.edu365.enumeration.PaymentStatus;
import fr.imcoding.edu365.enumeration.PaymentType;
import java.util.Date;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 27/01/2023
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFeesPaymenrResponse {
  private PaymentFeesDetails paymentFeesDetails;
  private boolean exemptFromFees;

}
