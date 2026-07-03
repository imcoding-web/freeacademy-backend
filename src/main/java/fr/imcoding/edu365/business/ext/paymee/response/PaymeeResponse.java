package fr.imcoding.edu365.business.ext.paymee.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 11/12/2022
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymeeResponse {
  private boolean status;
  private String message;
  private Integer code;
  private PaymeeDataResponse data;

}
