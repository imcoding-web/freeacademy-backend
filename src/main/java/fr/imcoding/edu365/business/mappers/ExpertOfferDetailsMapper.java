package fr.imcoding.edu365.business.mappers;

import fr.imcoding.edu365.business.services.UserAdditionalInfoService;
import fr.imcoding.edu365.client.dtos.response.User_Details;
import fr.imcoding.edu365.dtos.ExpertDetailsForOffer;
import fr.imcoding.edu365.dtos.UserDetails;
import fr.imcoding.edu365.persistence.entities.InformationGiver;
import fr.imcoding.edu365.persistence.entities.UserAdditionalInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.UserDetailsManagerConfigurer.UserDetailsBuilder;
import org.springframework.stereotype.Component;

/**
 * @author Rokaya
 * @Date 22/08/2022
 */
@Component
@RequiredArgsConstructor
public class ExpertOfferDetailsMapper {
  private final UserMapper userMapper;
  private final PositionMapper positionMapper;
  private final SpecialityMapper specialityMapper;
  private final UserAdditionalInfoService userAdditionalInfoService;



  public ExpertDetailsForOffer toExpertDetailsForOffer(InformationGiver user){
    UserDetails userDetails = userMapper.toUserDetailsResponse(user);
    UserAdditionalInfo userAdditionalInfo=userAdditionalInfoService.getUserAdditionalInfo(user.getUuid());
    return new ExpertDetailsForOffer(userDetails.getUserUuid(),
        userDetails.getUserFirstName(),userDetails.getUserLastName(),
        userDetails.getUserProfilePicture(),
        user.getUserPosition()!=null?positionMapper.toPositionDto(user.getUserPosition()).getPositionLabel():((userAdditionalInfo!=null&&userAdditionalInfo.getCustomPosition()!=null)?userAdditionalInfo.getCustomPosition():null),
        user.getUserSpeciality()!=null?specialityMapper.toSpecialityDto(user.getUserSpeciality()).getSpecialityLabel():(userAdditionalInfo!=null && userAdditionalInfo.getCustomSpeciality()!=null)?userAdditionalInfo.getCustomSpeciality():null);

  }

  /*public ExpertDetailsForOffer toExpertDetailsForOfferDto(ExpertDetailsForOffer expertDetailsForOffer){
    return  ExpertDetailsForOffer.builder().userFirstName(expertDetailsForOffer.getUserFirstName()).userLastName(expertDetailsForOffer.getUserLastName()).build();
        }*/

}
