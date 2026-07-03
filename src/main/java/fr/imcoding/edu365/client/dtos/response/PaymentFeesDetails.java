package fr.imcoding.edu365.client.dtos.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import fr.imcoding.edu365.dtos.MediaDetails;
import fr.imcoding.edu365.enumeration.PaymentStatus;
import fr.imcoding.edu365.enumeration.PaymentType;
import java.util.Date;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 02/02/2023
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentFeesDetails {
  private UUID paymentUuid;

  private PaymentStatus paymentStatus;

  private PaymentType paymentType;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
  private Date paymentDate;

  private String refusalReason;

  private MediaDetails paymentMedia;
}
