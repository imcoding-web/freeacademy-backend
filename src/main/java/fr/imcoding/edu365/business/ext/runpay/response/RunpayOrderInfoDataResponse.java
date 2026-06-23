package fr.imcoding.edu365.business.ext.runpay.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 31/08/2022
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RunpayOrderInfoDataResponse {
  @JsonProperty("ID")
  public String id;
  public String amount;
  public RunpayOrderInfoCustomerResponse customer;
}
