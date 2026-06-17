package fr.imcoding.edu365.client.dtos.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.OptBoolean;
import fr.imcoding.edu365.utils.Constants;
import java.util.Date;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class UserAvailabilityRequestDto {

  private String userAvailabilityTitle;

  @JsonFormat(
      shape = JsonFormat.Shape.STRING,
      pattern = "HH:mm",
      timezone = Constants.DEFAULT_TIMEZONE,
      lenient = OptBoolean.FALSE)
  private Date userAvailabilityStartDate;

  @JsonFormat(
      shape = JsonFormat.Shape.STRING,
      pattern = "HH:mm",
      timezone = Constants.DEFAULT_TIMEZONE,
      lenient = OptBoolean.FALSE)
  private Date userAvailabilityEndDate;

  private String userAvailabilityDay;
  private int videoconferenceDuration;
  private UUID expertUuid;

}
