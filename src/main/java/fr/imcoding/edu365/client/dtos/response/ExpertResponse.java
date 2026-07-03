package fr.imcoding.edu365.client.dtos.response;

import fr.imcoding.edu365.dtos.MediaDetails;
import fr.imcoding.edu365.dtos.PositionDto;
import fr.imcoding.edu365.enumeration.AccountStatus;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 11/11/2022
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExpertResponse {

  private String identifier;
  private  String userFirstName;
  private  String userlastName;
  private  UUID  userUuid;
  private  String  userEmail;
  private  String  userPhoneNumber;
  private AccountStatus accountStatus;
  private boolean isValidatedAccount;
  private PositionDto position;
  private String customPosition;
  private MediaDetails profilePicture;


}
