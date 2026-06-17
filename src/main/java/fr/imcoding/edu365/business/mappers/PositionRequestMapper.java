package fr.imcoding.edu365.business.mappers;

import fr.imcoding.edu365.business.services.ExpertUtils;
import fr.imcoding.edu365.business.services.UserAdditionalInfoService;
import fr.imcoding.edu365.client.dtos.request.PositionRequest;
import fr.imcoding.edu365.client.dtos.response.PositionResponse;
import fr.imcoding.edu365.persistence.entities.InformationGiver;
import fr.imcoding.edu365.persistence.entities.UserAdditionalInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Rokaya
 * @Date 13/06/2022
 */
@Component
@RequiredArgsConstructor
public class PositionRequestMapper {

 private final PositionMapper positionMapper;
 private final SpecialityMapper specialityMapper;
 private final UserAdditionalInfoService userAdditionalInfoService;
 private final ExpertUtils expertUtils;
 private final DegreeMapper degreeMapper;



  public PositionResponse toPositionDetailsResponse(InformationGiver user) {
    UserAdditionalInfo userAdditionalInfo=userAdditionalInfoService.getUserAdditionalInfo(user.getUuid());
    return new PositionResponse(
        user.getUserPosition()!=null ? positionMapper.toPositionDto(user.getUserPosition()):null,
        user.getUserSpeciality()!=null? specialityMapper.toSpecialityDto(user.getUserSpeciality()):null,
        userAdditionalInfo!= null ? userAdditionalInfo.getLastGraduationYear():null,
        userAdditionalInfo!= null && userAdditionalInfo.getCurrentGraduation()!=null ? degreeMapper.todegreeDto(userAdditionalInfo.getCurrentGraduation()):null,
        userAdditionalInfo!= null && userAdditionalInfo.getLastGraduation()!=null ? degreeMapper.todegreeDto(userAdditionalInfo.getLastGraduation()):null,
        userAdditionalInfo!= null ? userAdditionalInfo.getCustomSpeciality():null,
        userAdditionalInfo!= null ? userAdditionalInfo.getCustomPosition():null,
        expertUtils.getCV(user),
        expertUtils.getCerteficate(user),
        expertUtils.getGraduation(user)

        );
  }


  public PositionResponse toPositionDetails(UserAdditionalInfo userAdditionalInfo,PositionRequest positionRequest) {
    return new PositionResponse(
        positionRequest.getUserPosition()!=null? (positionRequest.getUserPosition()):null,
        positionRequest.getUserSpeciality()!=null? (positionRequest.getUserSpeciality()):null,
        userAdditionalInfo!= null ? userAdditionalInfo.getLastGraduationYear():null,
        (userAdditionalInfo!= null && userAdditionalInfo.getCurrentGraduation()!=null) ? degreeMapper.todegreeDto(userAdditionalInfo.getCurrentGraduation()):null,
        (userAdditionalInfo!= null && userAdditionalInfo.getCurrentGraduation()!=null) ? degreeMapper.todegreeDto(userAdditionalInfo.getLastGraduation()):null,
        userAdditionalInfo!= null ? userAdditionalInfo.getCustomSpeciality():null,
        userAdditionalInfo!= null ? userAdditionalInfo.getCustomPosition():null,
        positionRequest.getCvDocument(),
        positionRequest.getCertificateDocument(),
        positionRequest.getGraduationDocument()


    );
  }


}
