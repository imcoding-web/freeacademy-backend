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
public class SobflousTransactionVerificationResponse {
	@JsonProperty("TRANSM_ID")
	public String TRANSM_ID;

	@JsonProperty("AMOUNT")
	public String AMOUNT;

	@JsonProperty("TRANSS_ID")
	public String TRANSS_ID;

	@JsonProperty("TRANSACTION_STATE")
	public String TRANSACTION_STATE;

	@JsonProperty("MESSAGE")
	public String MESSAGE;

	@JsonProperty("TOKEN")
	public String TOKEN;

	@JsonProperty("ID_PAIEMENT_CLIENT")
	public String ID_PAIEMENT_CLIENT;

	@JsonProperty("DISCOUNT")
	public String DISCOUNT;

	@JsonProperty("DISCOUNT_AMOUNT")
	public String DISCOUNT_AMOUNT;

}
