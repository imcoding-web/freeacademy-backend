package fr.imcoding.edu365.persistence.entities;

import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "edu365_skill_area")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkillArea extends BaseEntity {

  private static final long serialVersionUID = -3396533977457748676L;

  private String skillAreaLabel;
  private String skillAreaCode;


}
