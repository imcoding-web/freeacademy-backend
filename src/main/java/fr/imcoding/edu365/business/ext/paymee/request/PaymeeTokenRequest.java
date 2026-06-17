package fr.imcoding.edu365.business.ext.paymee.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 11/12/2022
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymeeTokenRequest {
  private Integer vendor;
  private Double amount;
  private String note;

}
