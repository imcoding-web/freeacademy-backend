package fr.imcoding.edu365.client.dtos.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.imcoding.edu365.dtos.AddressDto;
import fr.imcoding.edu365.dtos.SkillAreaDto;
import fr.imcoding.edu365.dtos.SpecialityDto;
import fr.imcoding.edu365.enumeration.RoleCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

  private String userEmail;
  private String userPhoneNumber;
  private String userPassword;
  private String userFirstName;
  private String userLastName;
  private RoleCode role;
  private AddressDto userAddress;
  private SpecialityDto userSpeciality;
  private String userIdentityType;
  private String userIdentityNumber;
  @JsonProperty
  private boolean isTermsAccepted;

  private SkillAreaDto skillLevel;
}
