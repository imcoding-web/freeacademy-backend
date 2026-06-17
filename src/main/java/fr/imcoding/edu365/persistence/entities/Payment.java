package fr.imcoding.edu365.persistence.entities;

import fr.imcoding.edu365.enumeration.PaymentStatus;
import fr.imcoding.edu365.enumeration.PaymentType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 05/10/2022
 */

@Entity
@Table(name = "edu365_payment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment extends BaseEntity {
  @Enumerated(EnumType.STRING)
  private PaymentStatus paymentStatus;

  @Enumerated(EnumType.STRING)
  private PaymentType paymentType;

  private String refusalReason;
  private Double amountPaid;

  @OneToOne
  private Offer offer;

  @OneToOne
  private Media paymentMedia;

  @OneToOne
  private UserFees userFees;


}
