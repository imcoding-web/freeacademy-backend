package fr.imcoding.edu365.business.ext.paymee.response;

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
public class PaymeeDataResponse {
  private String token;
  private Double amount;

}
