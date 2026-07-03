package fr.imcoding.edu365.dtos;

import fr.imcoding.edu365.client.dtos.response.ProjectDetails;
import fr.imcoding.edu365.client.dtos.response.UserAvailabilityResponseDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 06/12/2022
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class ExpertProfileDto {
  private String uniqueIdentifier;
  private String userFirstName;
  private String userLastName;
  private String userEmail;
  private AddressDto userAddress;
  private MediaDetails userProfilePicture;
  private MediaDetails userCoverPicture;
  private String expertPosition;
  private String expertSpeciality;
  private List<UserAvailabilityResponseDto> expertAvailabilities;
  private List<ProjectDetails> expertProjects;
  private List<UserSkillsDto> expertSkills;
  private ExpertDescription expertDescription;
  private boolean isValidatedAccount;
  private boolean isMyOwnAccount;
  private boolean homeServices;
  private String homeServicesDescription;



}
