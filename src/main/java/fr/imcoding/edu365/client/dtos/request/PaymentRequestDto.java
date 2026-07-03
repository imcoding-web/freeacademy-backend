package fr.imcoding.edu365.client.dtos.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.imcoding.edu365.persistence.entities.InformationGiver;
import java.util.Date;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 25/12/2022
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentRequestDto {
  private UUID uuid;
  private Double amount;
  private UUID expertUuid;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Africa/Tunis")
  private Date transferCreatedDate;

}
