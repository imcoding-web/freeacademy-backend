package fr.imcoding.edu365.client.dtos.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.OptBoolean;
import java.util.Date;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 21/08/2022
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfferRequest {
  private UUID offerUuid;
  private Double offerPrice;
  private String offerDescription;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm")
  private Date deadlineDeliveringCorrection;
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private Date expertStartAvailabilityDate;
  private Integer videoconferenceDuration;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
  private Date videoconferenceStartDate;
  @JsonFormat(
      shape = JsonFormat.Shape.STRING,
      pattern = "HH:mm",
      lenient = OptBoolean.FALSE)
  private Date videoconferenceStartTime;
  private String wordForExpert;



}