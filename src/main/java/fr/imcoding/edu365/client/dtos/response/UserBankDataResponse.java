package fr.imcoding.edu365.client.dtos.response;

import fr.imcoding.edu365.enumeration.AccountType;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 15/07/2022
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBankDataResponse {
  private UUID uuid;
  private String bankAccountOwner;
  private String rib;
  private String bankName;
  private String bankingAgency;
  private String accumulatedBalanceS;
  private String unpaidAccumulatedBalanceS;
  private AccountType accountType;
  private String numTelD17;


}
