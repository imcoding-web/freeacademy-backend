package fr.imcoding.edu365.persistence.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import fr.imcoding.edu365.enumeration.PackageStatus;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Rokaya
 * @Date 01/10/2023
 */
@Entity
@Table(name = "edu365_package_subscription")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PackageSubscription extends BaseEntity {

  @Enumerated(EnumType.STRING)
  private PackageStatus subscriptionStatus;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Africa/Tunis")
  @Temporal(TemporalType.DATE)
  private Date startDate;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Africa/Tunis")
  @Temporal(TemporalType.DATE)
  private Date endDate;
  @ManyToOne(cascade = CascadeType.MERGE)
  private InformationSeeker student;

  @ManyToOne(cascade = CascadeType.MERGE)
  private SkillAreaPackage skillAreaPackage;


}
