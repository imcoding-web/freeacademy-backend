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
 * @Date 05/12/2022
 */

@Entity
@Table(name = "edu365_contact_msg")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactMessage extends BaseEntity {
  private String subject;
  private String message;
  @ManyToOne(cascade = CascadeType.MERGE)
  private User sender;


}
