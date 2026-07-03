package fr.imcoding.edu365.business.services;

import fr.imcoding.edu365.business.mappers.UserAdditinalInfoMapper;
import fr.imcoding.edu365.dtos.UserAdditionalInfoDto;
import fr.imcoding.edu365.persistence.entities.User;
import fr.imcoding.edu365.persistence.entities.UserAdditionalInfo;
import fr.imcoding.edu365.persistence.repositories.UserAdditionalInfoRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Rokaya
 * @Date 16/06/2022
 */
@Service
@RequiredArgsConstructor
public class UserAdditionalInfoService {

  private final UserAdditinalInfoMapper userAdditinalInfoMapper;
  private final UserAdditionalInfoRepository userAdditionalInfoRepository;


  public UserAdditionalInfo getUserAdditionalInfo(UUID userUuid){

    return userAdditionalInfoRepository.findByUserUuid(userUuid);
  }

  public UserAdditionalInfo saveAdditionInfo(UserAdditionalInfo userAdditionalInfo){
    return userAdditionalInfoRepository.save(userAdditionalInfo);

  }

}
