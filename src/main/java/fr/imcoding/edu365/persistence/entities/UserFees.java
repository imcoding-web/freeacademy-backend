package fr.imcoding.edu365.persistence.entities;

import fr.imcoding.edu365.enumeration.FeesType;
import fr.imcoding.edu365.enumeration.PaymentStatus;
import fr.imcoding.edu365.enumeration.PaymentType;
import fr.imcoding.edu365.utils.Utils;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;
import javax.persistence.PostPersist;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 27/01/2023
 */
@Entity
@Table(name = "edu365_user_fees")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFees extends BaseEntity {

  private Date datePaiement;
  @Enumerated(EnumType.STRING)
  private PaymentStatus paymentStatus;
  @Enumerated(EnumType.STRING)
  private PaymentType paymentType;
  @Enumerated(EnumType.STRING)
  private FeesType feesType;
  private String refusalReason;
  private Integer amountPaid;
  private String  uniqueIdentifier;
  @OneToOne
  private InformationGiver user;

  @OneToOne
  private Media paymentMedia;

  @PostPersist
  public void prePersist() {
    this.uniqueIdentifier = Utils.formatNumber(super.getId());
  }
}
