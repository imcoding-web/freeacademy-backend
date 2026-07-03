package fr.imcoding.edu365.persistence.entities;

import fr.imcoding.edu365.enumeration.AccountType;
import fr.imcoding.edu365.utils.Utils;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 15/07/2022
 */
@Entity
@Table(name = "edu365_user_bank_data")
@Data
@NoArgsConstructor
public class UserBankData extends BaseEntity {
  private String bankAccountOwner;
  private String rib;
  private String bankName;
  private String bankingAgency;
  private double accumulatedBalance;
  private String accumulatedBalanceS;
  private double unpaidAccumulatedBalance;
  private String unpaidAccumulatedBalanceS;
  @Column(length = 32, columnDefinition = "varchar(32) default 'BANK_ACCOUNT'")
  @Enumerated(EnumType.STRING)
  private AccountType accountType;
  private String numTelD17;



  @OneToOne
  private InformationGiver user;

  @PrePersist @PreUpdate
  public void prePersist() {
    this.accumulatedBalanceS = Utils.convertAmountToStringWithSeperator(this.accumulatedBalance);
    this.unpaidAccumulatedBalanceS = Utils.convertAmountToStringWithSeperator(this.unpaidAccumulatedBalance);
  }
}
