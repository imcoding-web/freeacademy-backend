package fr.imcoding.edu365.dtos;

import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 20/06/2022
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class ExpertDetails extends UserDetails {
  private String userIdentityType;
  private String userIdentityNumber;
  private MediaDetails userIdentityPicture;
  private MediaDetails userCoverPicture;
  private MediaDetails userProfilePicture;

  private boolean isValidate;
  private String userIdentifier;

  @Builder(builderMethodName = "expertBuilder")
  public ExpertDetails(UUID userUuid,
       String userFirstName,
       String userLastName,
       Date userDateInscription,
       String userEmail,
       String userPhoneNumber,
       AddressDto userAddress,
       MediaDetails userProfilePicture,
       String userIdentityType,
       String userIdentityNumber,
       MediaDetails useridentityPicture,
       MediaDetails userCoverPicture,
      boolean isValidate,String userIdentifier){
    super(userUuid,userFirstName,userLastName,
        userDateInscription,
        userEmail,
        userPhoneNumber,
        userAddress,
        userProfilePicture
        );
    this.userIdentityType=userIdentityType;
    this.userIdentityNumber=userIdentityNumber;
    this.userIdentityPicture=useridentityPicture;
    this.userCoverPicture=userCoverPicture;
    this.userProfilePicture=userProfilePicture;
    this.isValidate=isValidate;

    this.userIdentifier=userIdentifier;

  }

}
