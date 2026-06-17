package fr.imcoding.edu365.dtos;

import fr.imcoding.edu365.client.dtos.response.User_Details;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 22/08/2022
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpertDetailsForOffer extends User_Details {
  private String expertPosition;
  private String expertSpeciality;
  public ExpertDetailsForOffer(
      UUID userUuid,
      String userFirstName,
      String userLastName,
      MediaDetails userProfilePicture,
      String expertPosition,
      String expertSpeciality
      ){

    super(userUuid,userFirstName,userLastName,userProfilePicture);
    this.expertPosition=expertPosition;
    this.expertSpeciality=expertSpeciality;
  }






}
