package fr.imcoding.edu365.persistence.entities;

import fr.imcoding.edu365.enumeration.EmailContext;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rokaya
 * @Date 07/11/2022
 */
@Entity
@Table(name = "edu365_not_sent_email_traceability")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotSentEmailTraceability extends BaseEntity{

  private String email;
  @Enumerated(EnumType.STRING)
  private EmailContext context;
  private int attemptsNumber;
  private String subject;
  private String templateName;
  private String extraInformation;

  @PrePersist
  public void prePersist() {
    this.attemptsNumber = 0;
  }



}
