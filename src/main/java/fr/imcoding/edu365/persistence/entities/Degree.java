package fr.imcoding.edu365.persistence.entities;

import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 25/09/2022
 */
@Entity
@Table(name = "edu365_degree")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Degree extends BaseEntity{
  private String degreeLabel;
  private String degreeCode;

  @PrePersist
  public void prePersist() {
    this.degreeCode = this.degreeLabel.trim().toUpperCase().replace(" ","_");
  }
}
