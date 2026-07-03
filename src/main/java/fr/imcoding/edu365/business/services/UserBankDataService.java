package fr.imcoding.edu365.business.services;

import fr.imcoding.edu365.dtos.UserBankDataDto;
import fr.imcoding.edu365.persistence.entities.InformationGiver;
import fr.imcoding.edu365.persistence.entities.UserBankData;
import fr.imcoding.edu365.persistence.repositories.UserBankDataRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author Rokaya
 * @Date 15/07/2022
 */
@Service
@RequiredArgsConstructor
public class UserBankDataService {
  private final UserBankDataRepository userBankDataRepository;
  @Value("${edu365.prestation.commission}")
  private Integer prestationComission;

  public UserBankData getUserBankData(UUID userUuid){

    return userBankDataRepository.findByUserUuid(userUuid);
  }

  public UserBankData saveUserBankData(UserBankData userBankData){
    return userBankDataRepository.save(userBankData);
  }

  public UserBankData initExpertBankData(InformationGiver expert){
    UserBankData userBankData=new UserBankData();
    userBankData.setAccumulatedBalance(0);
    userBankData.setUnpaidAccumulatedBalance(0);
    userBankData.setUser(expert);
    return userBankDataRepository.save(userBankData);
  }

  public UserBankData updateUserBankData(UserBankDataDto userBankDataDto){
    UserBankData userBankData=getUserBankData(userBankDataDto.getExpertUuid());
    if(userBankData == null) {
      userBankData = new UserBankData();
    }
    userBankData.setUnpaidAccumulatedBalance(userBankData.getUnpaidAccumulatedBalance()+(userBankDataDto.getAmmount()-userBankDataDto.getAmmount()*prestationComission/100));
    userBankData.setAccumulatedBalance(userBankData.getAccumulatedBalance()+(userBankDataDto.getAmmount()-userBankDataDto.getAmmount()*prestationComission/100));

    return userBankDataRepository.save(userBankData);
  }

}
