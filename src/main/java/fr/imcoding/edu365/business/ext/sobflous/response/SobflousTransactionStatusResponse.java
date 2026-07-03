package fr.imcoding.edu365.business.ext.sobflous.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 02/01/2023
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SobflousTransactionStatusResponse {
  @JsonProperty("MERCHANT_TRANS_ID")
  public String MERCHANT_TRANS_ID;

  @JsonProperty("AMOUNT")
  public String AMOUNT;

  @JsonProperty("SOBFLOUS_TRANS_ID")
  public String SOBFLOUS_TRANS_ID;

  @JsonProperty("TRANSACTION_STATE")
  public String TRANS_STATE;
}
