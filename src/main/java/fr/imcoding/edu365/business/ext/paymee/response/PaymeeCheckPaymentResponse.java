package fr.imcoding.edu365.business.ext.paymee.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 12/12/2022
 */

@AllArgsConstructor
@Data
@NoArgsConstructor
public class PaymeeCheckPaymentResponse {
  private boolean status;
  private String message;
  private Integer code;
  private PaymeeDataCheckPaymentResponse data;

}
