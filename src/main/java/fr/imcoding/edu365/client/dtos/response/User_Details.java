package fr.imcoding.edu365.client.dtos.response;

import fr.imcoding.edu365.dtos.MediaDetails;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 22/08/2022
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User_Details {
  private UUID userUuid;
  private String userFirstName;
  private String userLastName;
  private String userDescription;
  private MediaDetails userProfilePicture;
  private String currentSchool;

  public User_Details(UUID userUuid, String userFirstName, String userLastName,
      MediaDetails userProfilePicture) {
    this.userUuid = userUuid;
    this.userFirstName = userFirstName;
    this.userLastName = userLastName;
    this.userProfilePicture = userProfilePicture;
  }
}
