package fr.imcoding.edu365.persistence.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "edu365_skill")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Skill extends BaseEntity {


  private static final long serialVersionUID = 2972057147074069345L;

  private String skillLabel;
  private String skillCode;
  @ManyToOne
  private SkillArea skillArea;
}
