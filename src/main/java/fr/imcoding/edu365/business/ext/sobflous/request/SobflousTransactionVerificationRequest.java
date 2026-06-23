package fr.imcoding.edu365.business.ext.sobflous.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 31/08/2022
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SobflousTransactionVerificationRequest {
	private String TRANSM_ID;
	private String TOKEN;
	private String SHOP_ID;
}
