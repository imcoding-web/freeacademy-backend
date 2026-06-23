package fr.imcoding.edu365.business.ext.sobflous.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 31/08/2022
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SobflousTokenResponse {
  @JsonProperty("TRANSM_ID")
  public String TRANSM_ID;
  @JsonProperty("AMOUNT")
  public String AMOUNT;
  @JsonProperty("DISCOUNT")
  public int DISCOUNT;
  @JsonProperty("DISCOUNT_AMOUNT")
  public int DISCOUNT_AMOUNT;
  @JsonProperty("TRANSS_ID")
  public String TRANSS_ID;
  @JsonProperty("URL")
  public String URL;
  @JsonProperty("TOKEN")
  public String TOKEN;
  @JsonProperty("CODE_COMMANDE")
  public String CODE_COMMANDE;
  @JsonProperty("URL_MOBILE")
  public String URL_MOBILE;
}
