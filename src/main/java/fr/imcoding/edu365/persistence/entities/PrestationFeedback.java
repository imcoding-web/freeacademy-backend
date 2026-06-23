package fr.imcoding.edu365.persistence.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

/**
 * @author Rokaya
 * @Date 12/01/2023
 */
@Entity
@Table(name = "edu365_prestation_feedback")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrestationFeedback extends BaseEntity {
  private int rate;

  @Column(columnDefinition = "TEXT")
  @Type(type = "text")
  private String message;
  @ManyToOne
  private Prestation prestation;

}
