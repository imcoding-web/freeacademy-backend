package fr.imcoding.edu365.business.mappers;

import fr.imcoding.edu365.dtos.UserAdditionalInfoDto;
import fr.imcoding.edu365.persistence.entities.UserAdditionalInfo;
import fr.imcoding.edu365.persistence.repositories.UserAdditionalInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Rokaya
 * @Date 16/06/2022
 */
@Service
@RequiredArgsConstructor
public class UserAdditinalInfoMapper {
  private final UserAdditionalInfoRepository userAdditionalInfoRepository;
  private final DegreeMapper degreeMapper;


  public UserAdditionalInfo toUserAdditionaInfo(UserAdditionalInfoDto userAdditionalInfoDto) {
    return userAdditionalInfoRepository.findByUuid(userAdditionalInfoDto.getUuid());
  }



  public UserAdditionalInfoDto toAdditionalInfoDto(UserAdditionalInfo userAdditionalInfo) {
    return new UserAdditionalInfoDto(
        userAdditionalInfo.getUuid(),
        userAdditionalInfo.getLastGraduationYear(),
        userAdditionalInfo!= null ? degreeMapper.todegreeDto(userAdditionalInfo.getCurrentGraduation()):null,
        userAdditionalInfo!= null ? degreeMapper.todegreeDto(userAdditionalInfo.getLastGraduation()):null,
        userAdditionalInfo.getCustomSpeciality());
  }

}
