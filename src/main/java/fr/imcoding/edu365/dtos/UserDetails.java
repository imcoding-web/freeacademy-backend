package fr.imcoding.edu365.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDetails {

  private UUID userUuid;
  private String userFirstName;
  private String userLastName;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private Date userDateInscription;
  private String userEmail;
  private String userPhoneNumber;
  private AddressDto userAddress;
  private MediaDetails userProfilePicture;


}
