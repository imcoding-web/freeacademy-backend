package fr.imcoding.edu365.dtos;

import fr.imcoding.edu365.enumeration.PaymentStatus;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 16/07/2022
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidationAccountResponse {
  private List<VerifiedFieldDto> fieldDtoList;
  private boolean verificationStatus;
  private boolean isPartialYet;
  private PaymentStatus paymentStatus;
  private boolean exemptFromFees;




}
