package fr.imcoding.edu365.client.dtos.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import fr.imcoding.edu365.dtos.ExpertDetailsForOffer;
import fr.imcoding.edu365.enumeration.OfferStatus;
import fr.imcoding.edu365.persistence.entities.Offer;
import java.util.Date;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 21/08/2022
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class OfferResponse {
  private UUID offerUuid;
  private Double offerPrice;
  private String offerDescription;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss")
  private Date deadlineDeliveringCorrection;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
  private Date expertStartAvailabilityDate;
  private Integer videoconferenceDuration;
  private ExpertDetailsForOffer offerGiver;
  private OfferStatus offerStatus;
  private String wordForAdvertiser;
  private UUID announcementUuid;
  private String announcementTitle;


}