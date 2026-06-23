package fr.imcoding.edu365.business.mappers;

import fr.imcoding.edu365.business.services.ExpertUtils;
import fr.imcoding.edu365.business.services.ProjectService;
import fr.imcoding.edu365.business.services.UserAdditionalInfoService;
import fr.imcoding.edu365.business.services.UserAvailabilityService;
import fr.imcoding.edu365.business.services.UserService;
import fr.imcoding.edu365.business.services.UserUtils;
import fr.imcoding.edu365.business.services.VerifiedExpertService;
import fr.imcoding.edu365.client.dtos.response.ExpertResponse;
import fr.imcoding.edu365.dtos.ExpertDetails;
import fr.imcoding.edu365.dtos.ExpertProfileDto;
import fr.imcoding.edu365.dtos.UserDetails;
import fr.imcoding.edu365.enumeration.MediaContext;
import fr.imcoding.edu365.persistence.entities.InformationGiver;
import fr.imcoding.edu365.persistence.entities.UserAdditionalInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Rokaya
 * @Date 20/06/2022
 */
@Service
@RequiredArgsConstructor
public class ExpertDetailsMapper {

  private final UserMapper userMapper;
  private final ExpertUtils expertUtils;
  private final UserUtils userUtils;

  private final PositionMapper positionMapper;
  private final VerifiedExpertService verifiedExpertService;
  private final SpecialityMapper specialityMapper;
  private final UserAvailabilityService userAvailabilityService;
  private final ProjectService projectService;
  private final AddressMapper addressMapper;
  private final UserService userService;
  private final ExpertDescriptionMapper expertDescriptionMapper;
  private final UserAdditionalInfoService userAdditionalInfoService;








  public ExpertDetails toExpertDetailsResponse(InformationGiver user,boolean accountStatus) {

    UserDetails userDetails = userMapper.toUserDetailsResponse(user);

    return new ExpertDetails(
        userDetails.getUserUuid(),
        userDetails.getUserFirstName(),
        userDetails.getUserLastName(),
        userDetails.getUserDateInscription(),
        userDetails.getUserEmail(),
        userDetails.getUserPhoneNumber(),
        userDetails.getUserAddress(),
        userDetails.getUserProfilePicture(),
        user.getIdentityType(),
        user.getIdentityNumber(),
        expertUtils.getPictureCIN(user),
        expertUtils.getCoverPicture(user),
        accountStatus,user.getUniqueIdentifier());


  }

  public ExpertResponse toExpertResponse(InformationGiver user) {

    //UserDetails userDetails = userMapper.toUserDetailsResponse(user);
    UserAdditionalInfo userAdditionalInfo=userAdditionalInfoService.getUserAdditionalInfo(user.getUuid());
    return  ExpertResponse.builder().userUuid(user.getUuid())
        .userFirstName(user.getUserFirstName())
        .userlastName(user.getUserLastName())
        .identifier(user.getUniqueIdentifier())
        .accountStatus(user.getAccountStatus())
        .userEmail(user.getUserEmail())
        .userPhoneNumber(user.getUserPhoneNumber())
        .position(user.getUserPosition()!=null?positionMapper.toPositionDto(user.getUserPosition()):null)
        .customPosition(userAdditionalInfo!=null && userAdditionalInfo.getCustomPosition()!=null?userAdditionalInfo.getCustomPosition():null)
        .isValidatedAccount(verifiedExpertService.checkCompletedValidation(user.getUuid()))
        .profilePicture(userUtils.getPictureProfile(user))
        .build();

  }
  public ExpertProfileDto toExpertProfileDto(InformationGiver expert,boolean isMyOwnAccount){
    UserAdditionalInfo userAdditionalInfo=userAdditionalInfoService.getUserAdditionalInfo(expert.getUuid());

    return  ExpertProfileDto.builder().uniqueIdentifier(expert.getUniqueIdentifier()).
        userFirstName(expert.getUserFirstName()).
        userLastName(expert.getUserLastName()).
        userEmail(expert.getUserEmail()).
        userAddress(expert.getUserAddress()!=null?addressMapper.toAddressDto(expert.getUserAddress()):null).
        userProfilePicture(userUtils.getPictureProfile(expert)).
        userCoverPicture(expertUtils.getCoverPicture(expert)).
        expertPosition(expert.getUserPosition()!=null?positionMapper.toPositionDto(expert.getUserPosition()).getPositionLabel():(userAdditionalInfo!=null && userAdditionalInfo.getCustomPosition()!=null)?userAdditionalInfo.getCustomPosition():null).
        expertSpeciality(expert.getUserSpeciality()!=null?specialityMapper.toSpecialityDto(expert.getUserSpeciality()).getSpecialityLabel():(userAdditionalInfo!=null && userAdditionalInfo.getCustomSpeciality()!=null)?userAdditionalInfo.getCustomSpeciality():null).
        expertAvailabilities(userAvailabilityService.getUserAvailabilitiesByUserUuid(expert.getUuid())).
        expertProjects(projectService.getAllByUserUuid(expert.getUuid())).
        expertSkills(userService.getSkillsUserUuid(expert.getUuid())).
        expertDescription(expertDescriptionMapper.toExpertDescription(expert)).
        isValidatedAccount(verifiedExpertService.checkCompletedValidation(expert.getUuid())).
        isMyOwnAccount(isMyOwnAccount).homeServices(expert.isHomeServices())
        .homeServicesDescription(expert.getHomeServicesDescription()).build();










  }

}
