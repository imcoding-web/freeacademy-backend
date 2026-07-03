package fr.imcoding.edu365.client.dtos.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import fr.imcoding.edu365.dtos.ExpertDetailsForOffer;
import fr.imcoding.edu365.dtos.MediaDetails;
import fr.imcoding.edu365.dtos.OfferResponseDto;
import fr.imcoding.edu365.enumeration.FeesType;
import fr.imcoding.edu365.enumeration.PaymentStatus;
import fr.imcoding.edu365.enumeration.PaymentType;
import java.util.Date;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 31/01/2023
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentFeesResponse {
  private UUID paymentUuid;

  private PaymentStatus paymentStatus;

  private PaymentType paymentType;
  private FeesType feesType;
  private ExpertDetailsForOffer expert;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
  private Date paymentDate;

  private String refusalReason;


  private MediaDetails paymentMedia;

}
