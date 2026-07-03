package fr.imcoding.edu365.persistence.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 09/06/2022
 */
@Entity
@Table(name = "edu365_speciality")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Speciality extends BaseEntity  {

  private static final long serialVersionUID = 9083326084374535654L;

  private String specialityLabel;
  private String specialityCode;


}
