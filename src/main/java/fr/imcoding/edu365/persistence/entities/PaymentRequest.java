package fr.imcoding.edu365.persistence.entities;

import fr.imcoding.edu365.enumeration.PaymentRequestStatus;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 25/12/2022
 */
@Entity
@Table(name = "edu365_payment_request")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest extends BaseEntity {
  private Double amount;
  @ManyToOne(cascade = CascadeType.MERGE)
  private InformationGiver expert;
  @Enumerated(EnumType.STRING)
  private PaymentRequestStatus paymentRequestStatus;

}
