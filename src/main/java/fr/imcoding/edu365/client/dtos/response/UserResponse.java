package fr.imcoding.edu365.client.dtos.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import fr.imcoding.edu365.dtos.AddressDto;
import fr.imcoding.edu365.dtos.MediaDetails;
import fr.imcoding.edu365.enumeration.PackageType;
import java.util.Date;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {

  private UUID userUuid;
  private String userFirstName;
  private String userLastName;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Africa/Tunis")
  private Date userDateInscription;
  private String userEmail;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Africa/Tunis")
  private Date userBirthDate;
  private AddressDto userAddress;
  private MediaDetails userProfilePicture;
  private Boolean hasAnActivePack; // if it is student else null
  private PackageType packageType;
}
