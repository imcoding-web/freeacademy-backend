package fr.imcoding.edu365.persistence.entities;

import fr.imcoding.edu365.enumeration.RoleCode;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "edu365_role")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role extends BaseEntity {

  /**
   *
   */
  private static final long serialVersionUID = 9083326084374535654L;

  private String roleLabel;
  @Enumerated(EnumType.STRING)
  private RoleCode roleCode;
}
