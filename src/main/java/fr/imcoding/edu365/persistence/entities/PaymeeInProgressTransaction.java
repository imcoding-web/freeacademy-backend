package fr.imcoding.edu365.persistence.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import fr.imcoding.edu365.enumeration.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 27/12/2022
 */

@Entity
@Table(name = "paymee_Inprogress_Transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymeeInProgressTransaction extends BaseEntity {
  private String token;
  @ManyToOne(cascade = CascadeType.MERGE)
  private Offer offer;

  @ManyToOne(cascade = CascadeType.MERGE)
  private User user;
  
  @Enumerated(EnumType.STRING)
  private TransactionType transactionType;

}
