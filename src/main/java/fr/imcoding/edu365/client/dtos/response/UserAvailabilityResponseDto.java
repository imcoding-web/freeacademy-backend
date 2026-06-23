package fr.imcoding.edu365.client.dtos.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.OptBoolean;
import fr.imcoding.edu365.enumeration.DaysOfWeek;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAvailabilityResponseDto {

  private UUID userAvailabilityUuid;

  private String userAvailabilityTitle;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss", lenient = OptBoolean.FALSE)
  private LocalDateTime userAvailabilityStartDate;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss", lenient = OptBoolean.FALSE)
  private LocalDateTime userAvailabilityEndDate;

  private DaysOfWeek userAvailabilityDay;
}
