package fr.imcoding.edu365.persistence.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 16/06/2022
 */
@Entity
@Table(name = "edu365_user_additional_info")
@Data
@NoArgsConstructor
public class UserAdditionalInfo extends BaseEntity{
  private String lastGraduationYear;
  @ManyToOne
  private Degree currentGraduation;
  @ManyToOne
  private Degree lastGraduation;
  private String customSpeciality;
  private String customPosition;

  @OneToOne
  private User user;

}
