package fr.imcoding.edu365.persistence.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "edu365_admin")
@Data
@NoArgsConstructor
@DiscriminatorValue("admin")
public class Administrator extends User {

  /**
   *
   */
  private static final long serialVersionUID = 5750899583047264016L;

}
