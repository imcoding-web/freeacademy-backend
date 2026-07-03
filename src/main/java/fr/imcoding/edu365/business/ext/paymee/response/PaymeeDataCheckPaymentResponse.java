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
public class PaymeeDataCheckPaymentResponse extends PaymeeDataResponse {
  private boolean payment_status;
  private Integer transaction_id;
  private Integer buyer_id;
  private Double received_amount;

}
