package fr.imcoding.edu365.persistence.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "edu365_skill_area_section")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkillAreaSection extends BaseEntity {

  private static final long serialVersionUID = -3396533977457748676L;

  private String label;
  private String code;




}
