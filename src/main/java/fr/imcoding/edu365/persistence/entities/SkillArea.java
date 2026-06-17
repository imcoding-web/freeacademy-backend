package fr.imcoding.edu365.persistence.entities;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "edu365_skill_area")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkillArea extends BaseEntity {

  private static final long serialVersionUID = -3396533977457748676L;

  private String skillAreaLabel;
  private String skillAreaCode;

  private Boolean shouldBeDisplayed;

  @ManyToMany
  @LazyCollection(LazyCollectionOption.FALSE)
  private List<SkillAreaSection> sections = new ArrayList<>();


}
