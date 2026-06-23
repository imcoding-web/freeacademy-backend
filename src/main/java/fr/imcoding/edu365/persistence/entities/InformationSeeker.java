package fr.imcoding.edu365.persistence.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import fr.imcoding.edu365.enumeration.AccountStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "edu365_information_seeker")
@Data
@NoArgsConstructor
@DiscriminatorValue("information_seeker")
public class InformationSeeker extends User {

  /**
   *
   */
  @ManyToOne
  private SkillArea currentLevel;
  @ManyToOne
  private SkillAreaSection currentLevelSection;
  private static final long serialVersionUID = 8989179485955903584L;
  
  @PrePersist
  public void preInformationSeekerPersist() {
    this.accountStatus = AccountStatus.ACTIVE;
  }

}
