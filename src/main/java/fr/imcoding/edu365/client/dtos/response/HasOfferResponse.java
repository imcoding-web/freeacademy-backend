package fr.imcoding.edu365.client.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HasOfferResponse {
	private boolean hasAnnouncementOffer;
	private boolean announcementInPackage;
	private Integer estimatedPriceToPay;
}
