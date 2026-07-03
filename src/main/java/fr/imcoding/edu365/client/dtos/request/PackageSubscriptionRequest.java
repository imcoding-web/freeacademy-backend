package fr.imcoding.edu365.client.dtos.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import fr.imcoding.edu365.enumeration.PackageType;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 01/10/2023
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PackageSubscriptionRequest {
  private UUID studentUuid;
  private int monthsNumber;
  private PackageType packageType;
  private String skillCode;



}
