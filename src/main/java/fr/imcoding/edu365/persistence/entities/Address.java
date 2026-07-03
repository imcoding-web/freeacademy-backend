package fr.imcoding.edu365.persistence.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "edu365_address")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address extends BaseEntity {

  /**
   *
   */
  private static final long serialVersionUID = -7809681680581775611L;

  private String addressStreet;
  private Integer addressStreetNumber;
  private String addressPostalCode;
  @ManyToOne(cascade = CascadeType.MERGE)
  private City addressCity;
}
