package fr.imcoding.edu365.persistence.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import fr.imcoding.edu365.enumeration.PackageStatus;
import fr.imcoding.edu365.enumeration.PackageType;
import java.time.LocalDate;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 01/10/2023
 */
// table pour gérer les souscriptions pour un seul matiére
@Entity
@Table(name = "edu365_skill_subscription")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkillSubscription extends BaseEntity {

  @Enumerated(EnumType.STRING)
  private PackageStatus subscriptionStatus;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Africa/Tunis")
  private LocalDate startDate;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Africa/Tunis")
  private LocalDate endDate;
  @ManyToOne(cascade = CascadeType.MERGE)
  private InformationSeeker student;

  @Enumerated(EnumType.STRING)
  private PackageType packageType;

  @ManyToOne(cascade = CascadeType.MERGE)
  private SkillArea skillLevel;

  @ManyToOne(cascade = CascadeType.MERGE)
  private Skill skill;


}
