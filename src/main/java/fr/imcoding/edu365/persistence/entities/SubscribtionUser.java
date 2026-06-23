package fr.imcoding.edu365.persistence.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@NoArgsConstructor
@Table(name = "edu365_subscription")
public class SubscribtionUser extends BaseEntity {

  private static final long serialVersionUID = 9083326084374535654L;
  private String email;


}
