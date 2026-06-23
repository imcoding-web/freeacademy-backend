package fr.imcoding.edu365.business.mappers;

import fr.imcoding.edu365.business.services.UserBankDataService;
import fr.imcoding.edu365.client.dtos.response.UserBankDataResponse;
import fr.imcoding.edu365.persistence.entities.InformationGiver;
import fr.imcoding.edu365.persistence.entities.UserBankData;
import fr.imcoding.edu365.persistence.repositories.UserBankDataRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Rokaya
 * @Date 15/07/2022
 */
@Service
@RequiredArgsConstructor
public class UserBankDataMapper {
  private final UserBankDataService userBankDataService;

  public UserBankDataResponse toUserBankDataResponse(UUID userUuid) {
    UserBankData userBankData=userBankDataService.getUserBankData(userUuid);
    return userBankData!=null ? new UserBankDataResponse(
       userBankData.getUuid(),
       userBankData.getBankAccountOwner(),
       userBankData.getRib(),
       userBankData.getBankName(),
       userBankData.getBankingAgency(),
       userBankData.getAccumulatedBalanceS(),
       userBankData.getUnpaidAccumulatedBalanceS(),
        userBankData.getAccountType(),
        userBankData.getNumTelD17()
    ):null;
  }


  public UserBankDataResponse toUserBankDataResponse(UserBankData userBankData) {
    return new UserBankDataResponse(
        userBankData.getUuid(),
        userBankData.getBankAccountOwner(),
        userBankData.getRib(),
        userBankData.getBankName(),
        userBankData.getBankingAgency(),
        userBankData.getAccumulatedBalanceS(),
        userBankData.getUnpaidAccumulatedBalanceS(),
        userBankData.getAccountType(),
        userBankData.getNumTelD17()
    );
  }

}
